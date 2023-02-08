package ru.citeck.ecos.context.lib.auth

import ru.citeck.ecos.context.lib.auth.component.AuthComponent
import ru.citeck.ecos.context.lib.auth.component.SimpleAuthComponent
import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.SimpleAuthData
import ru.citeck.ecos.context.lib.func.UncheckedRunnable
import ru.citeck.ecos.context.lib.func.UncheckedSupplier

object AuthContext {

    var component: AuthComponent = SimpleAuthComponent()
        set(value) {
            if (!isRunAsSystem()) {
                error("Access denied")
            }
            field = value
        }

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
    fun getCurrentUserWithAuthorities(): List<String> {
        return getUserWithAuthorities(getCurrentFullAuth())
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
    fun getCurrentRunAsUserWithAuthorities(): List<String> {
        return getUserWithAuthorities(getCurrentRunAsAuth())
    }

    @JvmStatic
    fun runAsSystemJ(action: UncheckedRunnable) {
        runAsSystem { action.invoke() }
    }

    @JvmStatic
    fun <T> runAsSystemJ(action: UncheckedSupplier<T>): T {
        return runAsSystem { action.invoke() }
    }

    @JvmStatic
    fun isRunAsSystemOrAdmin(): Boolean {
        val auth = getCurrentRunAsAuth()
        return isSystemAuth(auth) || isAdminAuth(auth)
    }

    @JvmStatic
    fun isNotRunAsSystemOrAdmin(): Boolean {
        return !isRunAsSystemOrAdmin()
    }

    @JvmStatic
    fun isRunAsSystem(): Boolean {
        return isSystemAuth(getCurrentRunAsAuth())
    }

    @JvmStatic
    fun isNotRunAsSystem(): Boolean {
        return !isRunAsSystem()
    }

    @JvmStatic
    fun isRunAsAdmin(): Boolean {
        return isAdminAuth(getCurrentRunAsAuth())
    }

    @JvmStatic
    fun isSystemAuth(auth: AuthData): Boolean {
        return auth.getAuthorities().contains(AuthRole.SYSTEM) || auth.getUser() == AuthUser.SYSTEM
    }

    @JvmStatic
    fun isAdminAuth(auth: AuthData): Boolean {
        return auth.getAuthorities().contains(AuthRole.ADMIN)
    }

    @JvmStatic
    fun <T> runAsSystem(action: () -> T): T {
        return runAs(AuthUser.SYSTEM, getSystemAuthorities(), action)
    }

    @JvmStatic
    fun getSystemAuthorities(): List<String> {
        val systemAuthorities = ArrayList(component.getSystemAuthorities())
        systemAuthorities.add(AuthRole.SYSTEM)
        return systemAuthorities
    }

    @JvmStatic
    fun <T> runAsJ(user: String, action: UncheckedSupplier<T>): T {
        return runAs(user) { action.invoke() }
    }

    @JvmStatic
    fun runAsJ(user: String, action: UncheckedRunnable) {
        return runAs(user) { action.invoke() }
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
    fun <T> runAsJ(auth: AuthData, action: UncheckedSupplier<T>): T {
        return runAs(auth) { action.invoke() }
    }

    @JvmStatic
    fun runAsJ(auth: AuthData, action: UncheckedRunnable) {
        return runAs(auth) { action.invoke() }
    }

    @JvmStatic
    fun <T> runAs(auth: AuthData, action: () -> T): T {
        return component.runAs(auth, false, action)
    }

    @JvmStatic
    fun <T> runAsFullJ(user: String, authorities: List<String>, action: UncheckedSupplier<T>): T {
        return runAsFull(user, authorities) { action.invoke() }
    }

    @JvmStatic
    fun runAsFullJ(user: String, authorities: List<String>, action: UncheckedRunnable) {
        return runAsFull(user, authorities) { action.invoke() }
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
    fun <T> runAsFullJ(auth: AuthData, action: UncheckedSupplier<T>): T {
        return runAsFull(auth) { action.invoke() }
    }

    @JvmStatic
    fun runAsFullJ(auth: AuthData, action: UncheckedRunnable) {
        return runAsFull(auth) { action.invoke() }
    }

    @JvmStatic
    fun <T> runAsFull(auth: AuthData, action: () -> T): T {
        return component.runAs(auth, true, action)
    }

    private fun getUserWithAuthorities(auth: AuthData): List<String> {
        val user = auth.getUser()
        val authorities = auth.getAuthorities()
        return if (user.isEmpty() || authorities.contains(user)) {
            authorities
        } else {
            val result = ArrayList<String>(authorities.size + 1)
            result.add(user)
            result.addAll(authorities)
            result
        }
    }
}
