package ru.citeck.ecos.context.lib.spring.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.citeck.ecos.context.lib.ContextServiceFactory
import ru.citeck.ecos.context.lib.auth.AuthComponent
import javax.annotation.PostConstruct

@Configuration
open class ContextServiceFactoryConfig : ContextServiceFactory() {

    private lateinit var authComponentBean: AuthComponent

    @PostConstruct
    override fun init() {
        super.init()
    }

    @Bean
    override fun createAuthComponent(): AuthComponent {
        return authComponentBean
    }

    @Autowired
    fun setAuthComponent(authComponent: AuthComponent) {
        authComponentBean = authComponent
    }
}
