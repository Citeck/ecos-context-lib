package ru.citeck.ecos.context.lib.auth.data

open class TokenAuthData(
    user: String,
    authorities: List<String>,
    private val token: String
) : SimpleAuthData(user, authorities) {

    fun getToken(): String {
        return token
    }
}
