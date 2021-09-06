package ru.citeck.ecos.context.lib.auth

interface AuthComponent {

    fun getCurrentFullAuth(): AuthData

    fun getCurrentRunAsAuth(): AuthData

    fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T

    fun getSystemUser(): String
}
