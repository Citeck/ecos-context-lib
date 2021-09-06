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

    override fun getCurrentAuth(): AuthData {
        val authentication = getAuthentication() ?: return SimpleAuthData.EMPTY
        return SimpleAuthData(authentication.name, authentication.authorities.map { it.authority })
    }

    override fun getCurrentRunAsAuth(): AuthData {
        return getCurrentAuth()
    }

    override fun <T> runAs(auth: AuthData, action: () -> T): T {

        val prevAuthentication = getAuthentication()

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
        val newAuthentication = UsernamePasswordAuthenticationToken(principal, "", grantedAuthorities)
        try {
            setAuthentication(newAuthentication)
            return action.invoke()
        } finally {
            setAuthentication(prevAuthentication)
        }
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
