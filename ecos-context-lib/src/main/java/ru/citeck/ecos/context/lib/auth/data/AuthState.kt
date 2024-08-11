package ru.citeck.ecos.context.lib.auth.data

class AuthState(
    val fullAuth: AuthData = UndefinedAuth,
    val runAsAuth: AuthData = UndefinedAuth
) {
    companion object {
        val DEFAULT = AuthState()
    }
}
