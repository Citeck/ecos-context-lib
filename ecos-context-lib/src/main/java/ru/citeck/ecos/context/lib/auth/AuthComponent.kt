package ru.citeck.ecos.context.lib.auth

interface AuthComponent {

    fun getCurrentUser(): String

    fun getCurrentUserAuthorities(): List<String>

    fun getCurrentRunAsUser(): String

    fun getCurrentRunAsUserAuthorities(): List<String>

    fun <T> runAs(user: String, authorities: List<String>, action: () -> T): T

    fun getSystemUser(): String
}
