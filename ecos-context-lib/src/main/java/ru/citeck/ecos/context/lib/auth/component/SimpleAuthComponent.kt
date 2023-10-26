package ru.citeck.ecos.context.lib.auth.component

import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class SimpleAuthComponent(private val defaultAuth: AuthData = EmptyAuth) : AuthComponent {

    private val state = ThreadLocal.withInitial { AuthState() }

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {

        val state = state.get()

        if (full) {
            val runAsPrev = state.runAsAuth
            val fullPrev = state.fullAuth
            val customAuthBefore = state.customFullAuth
            try {
                state.customFullAuth = true
                state.fullAuth = auth
                state.runAsAuth = EmptyAuth
                return action.invoke()
            } finally {
                state.fullAuth = fullPrev
                state.customFullAuth = customAuthBefore
                state.runAsAuth = runAsPrev
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
