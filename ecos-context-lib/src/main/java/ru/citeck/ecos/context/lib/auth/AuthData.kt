package ru.citeck.ecos.context.lib.auth

interface AuthData {

    fun getUser(): String

    fun getAuthorities(): List<String>
}
