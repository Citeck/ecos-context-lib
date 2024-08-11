package ru.citeck.ecos.context.lib.auth.data

object UndefinedAuth : AbstractAuthData() {
    override fun getUser(): String = ""
    override fun getAuthorities(): List<String> = emptyList()
    override fun isEmpty(): Boolean = true
}
