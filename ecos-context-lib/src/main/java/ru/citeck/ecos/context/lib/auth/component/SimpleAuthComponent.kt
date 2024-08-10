package ru.citeck.ecos.context.lib.auth.component

import org.slf4j.MDC
import ru.citeck.ecos.context.lib.auth.AuthConstants
import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.AuthState
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class SimpleAuthComponent(private val defaultAuth: AuthData = EmptyAuth) : AuthComponent {

    private val state = ThreadLocal.withInitial { AuthState() }

    override fun setAuthState(authState: AuthState) {
        this.state.set(authState)
        val mdcUser = authState.fullAuth.getUser()
        if (mdcUser.isNotBlank()) {
            MDC.put(AuthConstants.MDC_USER_KEY, mdcUser)
        } else {
            MDC.remove(AuthConstants.MDC_USER_KEY)
        }
    }

    override fun getAuthState(): AuthState {
        return state.get()
    }

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {

        val stateBefore = state.get()

        if (full) {
            val mdcUserBefore = MDC.get(AuthConstants.MDC_USER_KEY)
            try {
                this.state.set(
                    AuthState(
                        fullAuth = auth,
                        runAsAuth = EmptyAuth,
                        customFullAuth = true
                    )
                )
                MDC.put(AuthConstants.MDC_USER_KEY, auth.getUser())
                return action.invoke()
            } finally {
                this.state.set(stateBefore)
                if (mdcUserBefore.isNullOrBlank()) {
                    MDC.remove(AuthConstants.MDC_USER_KEY)
                } else {
                    MDC.put(AuthConstants.MDC_USER_KEY, mdcUserBefore)
                }
            }
        } else {
            if (stateBefore.fullAuth.isEmpty()) {
                return runAs(auth, true, action)
            }
            try {
                this.state.set(stateBefore.with(runAsAuth = auth))
                return action.invoke()
            } finally {
                this.state.set(stateBefore)
            }
        }
    }

    override fun getCurrentFullAuth(): AuthData {
        val state = state.get()
        if (!state.customFullAuth) {
            return defaultAuth
        }
        return state.fullAuth
    }

    override fun getCurrentRunAsAuth(): AuthData {
        val runAs = state.get().runAsAuth
        return if (runAs.isEmpty()) {
            getCurrentFullAuth()
        } else {
            runAs
        }
    }

    override fun getSystemAuthorities(): List<String> {
        return emptyList()
    }
}
