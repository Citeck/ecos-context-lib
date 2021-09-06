package ru.citeck.ecos.context.lib.auth

interface AuthComponent {

    fun getCurrentAuth(): AuthData

    fun getCurrentRunAsAuth(): AuthData

    fun <T> runAs(auth: AuthData, action: () -> T): T

    fun getSystemUser(): String
}
