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
import ru.citeck.ecos.context.lib.auth.AuthContext

@Component
@ConditionalOnMissingBean(AuthComponent::class)
class SpringAuthComponent : AuthComponent {

    @Value("\${spring.application.name:}")
    private lateinit var springAppName: String

    override fun getCurrentUser(): String {
        return getAuthentication()?.name ?: ""
    }

    override fun getCurrentUserAuthorities(): List<String> {

        val authentication = getAuthentication() ?: return emptyList()

        return authentication.authorities
            .map { it.authority }
            .filter { it.isNotBlank() }
    }

    override fun getCurrentRunAsUser(): String {
        return getCurrentUser()
    }

    override fun getCurrentRunAsUserAuthorities(): List<String> {
        return getCurrentUserAuthorities()
    }

    override fun <T> runAs(user: String, authorities: List<String>, action: () -> T): T {

        val prevAuthentication = getAuthentication()

        val grantedAuthorities = authorities.map {
            SimpleGrantedAuthority(it)
        }
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
        return AuthContext.APP_PREFIX + springAppName
    }

    private fun setAuthentication(authentication: Authentication?) {
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun getAuthentication(): Authentication? {
        return SecurityContextHolder.getContext()?.authentication ?: return null
    }
}
