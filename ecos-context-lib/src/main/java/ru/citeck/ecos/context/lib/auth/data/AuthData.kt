package ru.citeck.ecos.context.lib.auth.data

interface AuthData {

    fun getUser(): String

    fun getAuthorities(): List<String>

    fun isEmpty(): Boolean
}
