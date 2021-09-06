package ru.citeck.ecos.context.lib.spring.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import ru.citeck.ecos.context.lib.auth.AuthComponent
import ru.citeck.ecos.context.lib.auth.AuthData
import ru.citeck.ecos.context.lib.auth.SimpleAuthData

@Component
@ConditionalOnMissingBean(AuthComponent::class)
class SpringAuthComponent : AuthComponent {

    companion object {
        const val APP_PREFIX = "APP_"
    }

    @Value("\${spring.application.name:}")
    private lateinit var springAppName: String

    private var fullAuth: ThreadLocal<Authentication> = ThreadLocal()

    override fun getCurrentFullAuth(): AuthData {
        return authenticationToAuthData(fullAuth.get() ?: getAuthentication())
    }

    override fun getCurrentRunAsAuth(): AuthData {
        return authenticationToAuthData(getAuthentication())
    }

    private fun authenticationToAuthData(authentication: Authentication?): AuthData {
        authentication ?: return SimpleAuthData.EMPTY
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

        val fullAuthorities = if (authorities.contains(user)) {
            authorities
        } else {
            val fullAuthorities = arrayListOf<String>()
            fullAuthorities.add(user)
            fullAuthorities.addAll(authorities)
            fullAuthorities
        }

        val grantedAuthorities = fullAuthorities
            .filter { it.isNotBlank() }
            .map { SimpleGrantedAuthority(it) }

        val principal = User(user, "", grantedAuthorities)
        return UsernamePasswordAuthenticationToken(principal, "", grantedAuthorities)
    }

    override fun getSystemUser(): String {
        return APP_PREFIX + springAppName
    }

    private fun setAuthentication(authentication: Authentication?) {
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun getAuthentication(): Authentication? {
        return SecurityContextHolder.getContext()?.authentication ?: return null
    }
}
