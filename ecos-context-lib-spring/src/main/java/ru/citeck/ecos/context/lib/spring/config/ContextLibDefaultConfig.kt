package ru.citeck.ecos.context.lib.spring.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.citeck.ecos.context.lib.auth.AuthComponent

@Configuration
open class ContextLibDefaultConfig {

    @Bean
    @ConditionalOnMissingBean(AuthComponent::class)
    open fun createAuthComponent(): AuthComponent {
        return SpringAuthComponent()
    }
}
