package ru.citeck.ecos.context.lib

import ru.citeck.ecos.context.lib.auth.AuthContext
import ru.citeck.ecos.context.lib.auth.component.AuthComponent
import ru.citeck.ecos.context.lib.i18n.I18nContext
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import ru.citeck.ecos.context.lib.time.TimeZoneContext
import ru.citeck.ecos.context.lib.time.component.TimeZoneComponent

open class ContextServiceFactory {

    open fun init() {
        AuthContext.component = createAuthComponent() ?: AuthContext.component
        TimeZoneContext.component = createTimeZoneComponent() ?: TimeZoneContext.component
        I18nContext.component = createI18nComponent() ?: I18nContext.component
    }

    protected open fun createAuthComponent(): AuthComponent? {
        return null
    }

    protected open fun createTimeZoneComponent(): TimeZoneComponent? {
        return null
    }

    protected open fun createI18nComponent(): I18nComponent? {
        return null
    }
}
