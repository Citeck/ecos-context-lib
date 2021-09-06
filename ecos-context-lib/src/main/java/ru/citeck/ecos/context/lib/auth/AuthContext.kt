package ru.citeck.ecos.context.lib.auth

object AuthContext {

    const val ROLE_SYSTEM = "ROLE_SYSTEM"
    const val APP_PREFIX = "APP_"

    lateinit var component: AuthComponent

    fun getCurrentUser(): String {
        return component.getCurrentUser()
    }

    fun getCurrentAuthorities(): List<String> {
        return component.getCurrentAuthorities()
    }

    fun getCurrentRunAsUser(): String {
        return component.getCurrentRunAsUser()
    }

    fun getCurrentRunAsAuthorities(): List<String> {
        return component.getCurrentRunAsAuthorities()
    }

    fun <T> runAs(user: String, authorities: List<String>, action: () -> T): T {
        return component.runAs(user, authorities, action)
    }

    fun <T> runAsSystem(action: () -> T): T {
        return component.runAs(component.getSystemUser(), listOf(ROLE_SYSTEM), action)
    }
}
