package ru.citeck.ecos.context.lib.i18n.component

import java.util.*

class SimpleI18nComponent : I18nComponent {

    companion object {
        val DEFAULT = listOf(Locale.ENGLISH)
    }

    private val current = ThreadLocal.withInitial { DEFAULT }

    override fun setLocales(locales: List<Locale>) {
        current.set(locales.ifEmpty { DEFAULT })
    }

    override fun <T> doWithLocales(locales: List<Locale>, action: () -> T): T {
        val prevValue = this.current.get()
        try {
            setLocales(locales)
            return action.invoke()
        } finally {
            this.current.set(prevValue)
        }
    }

    override fun getLocales(): List<Locale> {
        return current.get()
    }
}
