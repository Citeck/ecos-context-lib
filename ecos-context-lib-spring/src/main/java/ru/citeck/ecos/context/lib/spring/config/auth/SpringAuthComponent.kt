package ru.citeck.ecos.context.lib.spring.config.auth

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import ru.citeck.ecos.context.lib.auth.AuthConstants
import ru.citeck.ecos.context.lib.auth.component.AuthComponent
import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth
import ru.citeck.ecos.context.lib.auth.data.SimpleAuthData

class SpringAuthComponent : AuthComponent {

    private var springAppName: String = ""

    private var fullAuth: ThreadLocal<Authentication> = ThreadLocal()

    override fun getCurrentFullAuth(): AuthData {
        return authenticationToAuthData(fullAuth.get() ?: getAuthentication())
    }

    override fun getCurrentRunAsAuth(): AuthData {
        return authenticationToAuthData(getAuthentication())
    }

    private fun authenticationToAuthData(authentication: Authentication?): AuthData {
        authentication ?: return EmptyAuth
        return SimpleAuthData(authentication.name, authentication.authorities.map { it.authority })
    }

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {

        val prevFullAuth = fullAuth.get()
        val prevAuthentication = getAuthentication()
        val newAuthentication = buildAuthentication(auth)
        val fullAuthOwner = full || prevFullAuth == null

        try {
            if (fullAuthOwner) {
                fullAuth.set(newAuthentication)
            }
            setAuthentication(newAuthentication)
            return action.invoke()
        } finally {
            setAuthentication(prevAuthentication)
            if (fullAuthOwner) {
                if (prevFullAuth == null) {
                    fullAuth.remove()
                } else {
                    fullAuth.set(prevFullAuth)
                }
            }
        }
    }

    private fun buildAuthentication(auth: AuthData): Authentication {

        val user = auth.getUser()
        val authorities = auth.getAuthorities()

        val grantedAuthorities = authorities
            .filter { it.isNotBlank() }
            .map { SimpleGrantedAuthority(it) }

        val principal = User(user, "", grantedAuthorities)
        return UsernamePasswordAuthenticationToken(principal, "", grantedAuthorities)
    }

    override fun getSystemAuthorities(): List<String> {
        if (springAppName.isNotBlank()) {
            return listOf(AuthConstants.APP_PREFIX + springAppName)
        }
        return emptyList()
    }

    @Value("\${spring.application.name:}")
    fun setSpringAppName(name: String) {
        this.springAppName = name
    }

    private fun setAuthentication(authentication: Authentication?) {
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun getAuthentication(): Authentication? {
        return SecurityContextHolder.getContext()?.authentication ?: return null
    }
}
