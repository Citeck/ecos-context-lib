package ru.citeck.ecos.context.lib.spring.config.i18n

import org.springframework.context.i18n.LocaleContextHolder
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import java.util.*

class SpringI18nComponent : I18nComponent {

    override fun <T> doWithLocales(locales: List<Locale>, action: () -> T): T {
        val prevLocale = LocaleContextHolder.getLocale()
        try {
            LocaleContextHolder.setLocale(locales[0])
            return action.invoke()
        } finally {
            LocaleContextHolder.setLocale(prevLocale)
        }
    }

    override fun getLocales(): List<Locale> {
        return listOf(LocaleContextHolder.getLocale() ?: Locale.ENGLISH)
    }
}
