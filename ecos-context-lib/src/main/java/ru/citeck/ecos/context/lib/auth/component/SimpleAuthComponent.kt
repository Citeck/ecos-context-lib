package ru.citeck.ecos.context.lib.auth.component

import org.slf4j.MDC
import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class SimpleAuthComponent(private val defaultAuth: AuthData = EmptyAuth) : AuthComponent {

    companion object {
        private const val MDC_USER_KEY = "ecosUser"
    }

    private val state = ThreadLocal.withInitial { AuthState() }

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {

        val state = state.get()

        if (full) {
            val runAsPrev = state.runAsAuth
            val fullPrev = state.fullAuth
            val customAuthBefore = state.customFullAuth
            val mdcUserBefore = MDC.get(MDC_USER_KEY)
            try {
                state.customFullAuth = true
                state.fullAuth = auth
                state.runAsAuth = EmptyAuth
                MDC.put(MDC_USER_KEY, auth.getUser())
                return action.invoke()
            } finally {
                state.fullAuth = fullPrev
                state.customFullAuth = customAuthBefore
                state.runAsAuth = runAsPrev
                if (mdcUserBefore.isNullOrBlank()) {
                    MDC.remove(MDC_USER_KEY)
                } else {
                    MDC.put(MDC_USER_KEY, mdcUserBefore)
                }
            }
        } else {
            if (state.fullAuth.isEmpty()) {
                return runAs(auth, true, action)
            }
            val prevRunAs = state.runAsAuth
            try {
                state.runAsAuth = auth
                return action.invoke()
            } finally {
                state.runAsAuth = prevRunAs
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

    private class AuthState(
        var fullAuth: AuthData = EmptyAuth,
        var runAsAuth: AuthData = EmptyAuth,
        var customFullAuth: Boolean = false
    )
}
