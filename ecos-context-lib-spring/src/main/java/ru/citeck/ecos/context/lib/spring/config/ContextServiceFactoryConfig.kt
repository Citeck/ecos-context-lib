package ru.citeck.ecos.context.lib.spring.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import ru.citeck.ecos.context.lib.ContextServiceFactory
import ru.citeck.ecos.context.lib.auth.component.AuthComponent
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import javax.annotation.PostConstruct

@Configuration
open class ContextServiceFactoryConfig : ContextServiceFactory() {

    private lateinit var authComponentBean: AuthComponent
    private lateinit var i18nComponentBean: I18nComponent

    @PostConstruct
    override fun init() {
        super.init()
    }

    override fun createAuthComponent(): AuthComponent {
        return authComponentBean
    }

    override fun createI18nComponent(): I18nComponent? {
        return i18nComponentBean
    }

    @Autowired
    fun setAuthComponent(authComponent: AuthComponent) {
        authComponentBean = authComponent
    }

    @Autowired
    fun setI18nComponent(i18nComponent: I18nComponent) {
        i18nComponentBean = i18nComponent
    }
}
