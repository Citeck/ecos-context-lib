package ru.citeck.ecos.context.lib.auth

object AuthContext {

    const val ROLE_SYSTEM = "ROLE_SYSTEM"

    var component: AuthComponent = SimpleAuthComponent()

    @JvmStatic
    fun getCurrentAuth(): AuthData {
        return component.getCurrentAuth()
    }

    @JvmStatic
    fun getCurrentRunAsAuth(): AuthData {
        return component.getCurrentRunAsAuth()
    }

    @JvmStatic
    fun getCurrentUser(): String {
        return getCurrentAuth().getUser()
    }

    @JvmStatic
    fun getCurrentAuthorities(): List<String> {
        return getCurrentAuth().getAuthorities()
    }

    @JvmStatic
    fun getCurrentRunAsUser(): String {
        return getCurrentRunAsAuth().getUser()
    }

    @JvmStatic
    fun getCurrentRunAsAuthorities(): List<String> {
        return getCurrentRunAsAuth().getAuthorities()
    }

    @JvmStatic
    fun isRunAsSystem(): Boolean {
        return getCurrentRunAsAuthorities().contains(ROLE_SYSTEM)
    }

    @JvmStatic
    fun <T> runAsSystem(action: () -> T): T {
        return runAs(component.getSystemUser(), listOf(ROLE_SYSTEM), action)
    }

    @JvmStatic
    fun <T> runAs(user: String, action: () -> T): T {
        return runAs(user, emptyList(), action)
    }

    @JvmStatic
    fun <T> runAs(user: String, authorities: List<String>, action: () -> T): T {
        return runAs(SimpleAuthData(user, authorities), action)
    }

    @JvmStatic
    fun <T> runAs(auth: AuthData, action: () -> T): T {
        return component.runAs(auth, action)
    }
}
