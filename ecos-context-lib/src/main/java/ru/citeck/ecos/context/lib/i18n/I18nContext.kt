package ru.citeck.ecos.context.lib.i18n

import ru.citeck.ecos.context.lib.func.UncheckedRunnable
import ru.citeck.ecos.context.lib.func.UncheckedSupplier
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import ru.citeck.ecos.context.lib.i18n.component.SimpleI18nComponent
import java.util.*

object I18nContext {

    val RUSSIAN = Locale("ru")
    val ENGLISH = Locale.ENGLISH

    val DEFAULT = listOf(ENGLISH)

    var component: I18nComponent = SimpleI18nComponent()

    @JvmStatic
    fun doWithLocaleJ(locale: Locale?, action: UncheckedRunnable) {
        doWithLocale(locale) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithLocaleJ(locale: Locale?, action: UncheckedSupplier<T>): T {
        return doWithLocale(locale) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithDefaultLocale(action: () -> T): T {
        return doWithLocales(DEFAULT, action)
    }

    @JvmStatic
    fun <T> doWithLocale(locale: Locale?, action: () -> T): T {
        return doWithLocales(locale?.let { listOf(it) }, action)
    }

    @JvmStatic
    fun <T> doWithLocales(locales: List<Locale>?, action: () -> T): T {
        return component.doWithLocales(locales?.ifEmpty { DEFAULT } ?: DEFAULT, action)
    }

    @JvmStatic
    fun getLocale(): Locale {
        return getLocales()[0]
    }

    @JvmStatic
    fun getLocales(): List<Locale> {
        return component.getLocales().ifEmpty { DEFAULT }
    }

    @JvmStatic
    fun getDefaultLocale(): Locale {
        return DEFAULT[0]
    }

    @JvmStatic
    fun getDefaultLocales(): List<Locale> {
        return DEFAULT
    }
}
