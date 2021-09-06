package ru.citeck.ecos.context.lib.auth

interface AuthComponent {

    fun getCurrentUser(): String

    fun getCurrentAuthorities(): List<String>

    fun getCurrentRunAsUser(): String

    fun getCurrentRunAsAuthorities(): List<String>

    fun <T> runAs(user: String, authorities: List<String>, action: () -> T): T

    fun getSystemUser(): String
}
