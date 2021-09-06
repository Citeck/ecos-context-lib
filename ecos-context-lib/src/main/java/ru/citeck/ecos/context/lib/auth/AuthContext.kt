package ru.citeck.ecos.context.lib.auth

object AuthContext {

    var component: AuthComponent = SimpleAuthComponent()

    @JvmStatic
    fun getCurrentFullAuth(): AuthData {
        return component.getCurrentFullAuth()
    }

    @JvmStatic
    fun getCurrentRunAsAuth(): AuthData {
        return component.getCurrentRunAsAuth()
    }

    @JvmStatic
    fun getCurrentUser(): String {
        return getCurrentFullAuth().getUser()
    }

    @JvmStatic
    fun getCurrentAuthorities(): List<String> {
        return getCurrentFullAuth().getAuthorities()
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
        return getCurrentRunAsAuthorities().contains(AuthRole.SYSTEM)
    }

    @JvmStatic
    fun <T> runAsSystem(action: () -> T): T {
        return runAs(component.getSystemUser(), listOf(AuthRole.SYSTEM), action)
    }

    @JvmStatic
    fun getSystemUser(): String {
        return component.getSystemUser()
    }

    @JvmStatic
    fun runAsSystemJ(action: Runnable) {
        return runAsSystem { action.run() }
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
        return component.runAs(auth, false, action)
    }

    @JvmStatic
    fun <T> runAsFull(user: String, action: () -> T): T {
        return runAsFull(user, emptyList(), action)
    }

    @JvmStatic
    fun <T> runAsFull(user: String, authorities: List<String>, action: () -> T): T {
        return runAsFull(SimpleAuthData(user, authorities), action)
    }

    @JvmStatic
    fun <T> runAsFull(auth: AuthData, action: () -> T): T {
        return component.runAs(auth, true, action)
    }
}
