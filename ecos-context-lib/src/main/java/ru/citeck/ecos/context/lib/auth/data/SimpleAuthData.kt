package ru.citeck.ecos.context.lib.auth.data

open class SimpleAuthData(
    private val user: String,
    private val authorities: List<String>
) : AuthData {

    override fun getUser(): String {
        return user
    }

    override fun getAuthorities(): List<String> {
        return authorities
    }
}
