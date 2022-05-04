package ru.citeck.ecos.context.lib.spring.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.citeck.ecos.context.lib.auth.component.AuthComponent
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import ru.citeck.ecos.context.lib.spring.config.auth.SpringAuthComponent
import ru.citeck.ecos.context.lib.spring.config.i18n.SpringI18nComponent

@Configuration
open class ContextLibDefaultConfig {

    @Bean
    @ConditionalOnMissingBean(AuthComponent::class)
    open fun createAuthComponent(): AuthComponent {
        return SpringAuthComponent()
    }

    @Bean
    @ConditionalOnMissingBean(I18nComponent::class)
    open fun createI18nComponent(): I18nComponent {
        return SpringI18nComponent()
    }
}
