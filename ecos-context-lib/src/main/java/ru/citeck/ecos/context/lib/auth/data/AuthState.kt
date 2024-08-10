package ru.citeck.ecos.context.lib.auth.data

class AuthState(
    val fullAuth: AuthData = EmptyAuth,
    val runAsAuth: AuthData = EmptyAuth,
    val customFullAuth: Boolean = false
) {
    companion object {
        val DEFAULT = AuthState()
    }

    fun with(
        fullAuth: AuthData = this.fullAuth,
        runAsAuth: AuthData = this.runAsAuth,
        customFullAuth: Boolean = this.customFullAuth
    ): AuthState {
        return AuthState(fullAuth, runAsAuth, customFullAuth)
    }
}
