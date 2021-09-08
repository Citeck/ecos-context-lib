package ru.citeck.ecos.context.lib.auth.data

object EmptyAuth : AuthData {
    override fun getUser(): String = ""
    override fun getAuthorities(): List<String> = emptyList()
}
