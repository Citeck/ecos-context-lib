package ru.citeck.ecos.context.lib.i18n

import ru.citeck.ecos.context.lib.ctx.CtxScope
import ru.citeck.ecos.context.lib.ctx.GlobalEcosContext
import ru.citeck.ecos.context.lib.ctx.extval.SimpleCtxExtValue
import ru.citeck.ecos.context.lib.func.UncheckedRunnable
import ru.citeck.ecos.context.lib.func.UncheckedSupplier
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import ru.citeck.ecos.context.lib.i18n.component.SimpleI18nComponent
import java.util.*

object I18nContext {

    @JvmField
    val RUSSIAN = Locale("ru")
    @JvmField
    val ENGLISH = Locale.ENGLISH
    @JvmField
    val DEFAULT = listOf(ENGLISH)

    var component: I18nComponent = SimpleI18nComponent()

    init {
        GlobalEcosContext.register(
            I18nContext::class,
            object : SimpleCtxExtValue<List<Locale>> {
                override fun get(): List<Locale> {
                    return component.getLocales()
                }
                override fun set(value: List<Locale>?) {
                    return component.setLocales(value?.ifEmpty { DEFAULT } ?: DEFAULT)
                }
            }
        )
    }

    fun set(scope: CtxScope, locales: List<Locale>) {
        scope[I18nContext::class] = locales
    }

    fun set(scope: CtxScope, locale: Locale) {
        scope[I18nContext::class] = listOf(locale)
    }

    @JvmStatic
    fun doWithLocaleJ(locale: Locale?, action: UncheckedRunnable) {
        doWithLocale(locale) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithLocaleJ(locale: Locale?, action: UncheckedSupplier<T>): T {
        return doWithLocale(locale) { action.invoke() }
    }

    @JvmStatic
    fun doWithLocalesJ(locales: List<Locale>?, action: UncheckedRunnable) {
        doWithLocales(locales) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithLocalesJ(locales: List<Locale>?, action: UncheckedSupplier<T>): T {
        return doWithLocales(locales) { action.invoke() }
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
