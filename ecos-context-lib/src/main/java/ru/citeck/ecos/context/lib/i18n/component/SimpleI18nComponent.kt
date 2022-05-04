package ru.citeck.ecos.context.lib.i18n.component

import java.util.*

class SimpleI18nComponent : I18nComponent {

    companion object {
        val DEFAULT = listOf(Locale.ENGLISH)
    }

    private val current = ThreadLocal.withInitial { DEFAULT }

    override fun <T> doWithLocales(locales: List<Locale>, action: () -> T): T {
        val prevValue = this.current.get()
        try {
            this.current.set(locales.ifEmpty { DEFAULT })
            return action.invoke()
        } finally {
            this.current.set(prevValue)
        }
    }

    override fun getLocales(): List<Locale> {
        return current.get()
    }
}
