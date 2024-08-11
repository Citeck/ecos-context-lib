package ru.citeck.ecos.context.lib.auth.component

import org.slf4j.MDC
import ru.citeck.ecos.context.lib.auth.AuthConstants
import ru.citeck.ecos.context.lib.auth.AuthContext
import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.AuthState
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth
import ru.citeck.ecos.context.lib.auth.data.UndefinedAuth

class DefaultAuthComponent(
    private val defaultAuth: AuthData = EmptyAuth,
    private val runAsAuthHolder: AuthHolder? = null
) : AuthComponent {

    private val state = ThreadLocal.withInitial { AuthState() }

    fun getDefaultAuth(): AuthData {
        return defaultAuth
    }

    override fun setAuthState(authState: AuthState) {

        val fixedAuthState = if (authState.fullAuth.isEmpty() && authState.runAsAuth.isNotEmpty()) {
            AuthState(authState.runAsAuth)
        } else {
            authState
        }

        this.state.set(fixedAuthState)
        val mdcUser = fixedAuthState.fullAuth.ifUndefined { defaultAuth }.getUser()
        if (mdcUser.isNotBlank()) {
            MDC.put(AuthConstants.MDC_USER_KEY, mdcUser)
        } else {
            MDC.remove(AuthConstants.MDC_USER_KEY)
        }
        runAsAuthHolder?.set(getRunAsAuth(fixedAuthState))
    }

    override fun getAuthState(): AuthState {
        return state.get()
    }

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {

        val stateBefore = getAuthState()

        val newAuthState = if (auth == EmptyAuth) {
            AuthState(EmptyAuth, EmptyAuth)
        } else {
            val isFullRunAs = full || stateBefore.fullAuth.isEmpty()
            if (isFullRunAs) {
                AuthState(auth)
            } else {
                AuthState(stateBefore.fullAuth, auth)
            }
        }
        return try {
            setAuthState(newAuthState)
            action.invoke()
        } finally {
            // Component may be changed inside action,
            // and we should restore previous auth state for it.
            // In this case state variable become in inconsistent state,
            // but it's not a problem because if we register this component
            // back to AuthContext, then state will be updated
            // automatically with actual value
            AuthContext.component.setAuthState(stateBefore)
        }
    }

    override fun getCurrentFullAuth(): AuthData {
        return state.get().fullAuth
            .ifUndefined { defaultAuth }
    }

    override fun getCurrentRunAsAuth(): AuthData {
        return getRunAsAuth(state.get())
    }

    private fun getRunAsAuth(state: AuthState): AuthData {
        return state.runAsAuth
            .ifEmpty { state.fullAuth }
            .ifUndefined { defaultAuth }
    }

    private inline fun AuthData.ifUndefined(crossinline fallback: () -> AuthData): AuthData {
        return if (this is UndefinedAuth) fallback.invoke() else this
    }

    private inline fun AuthData.ifEmpty(crossinline fallback: () -> AuthData): AuthData {
        return if (this.isEmpty()) fallback.invoke() else this
    }
}
