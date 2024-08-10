package ru.citeck.ecos.context.lib.i18n

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.ctx.GlobalEcosContext
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class I18nContextTest {

    @Test
    fun ecosContextTest() {

        val data = I18nContext.doWithLocale(Locale.FRANCE) {
            assertThat(I18nContext.getLocale()).isEqualTo(Locale.FRANCE)
            GlobalEcosContext.getScopeData()
        }
        assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)

        println(data)

        GlobalEcosContext.newScope(data).use {
            assertThat(I18nContext.getLocale()).isEqualTo(Locale.FRANCE)
        }
        assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)

        GlobalEcosContext.newScope().use {
            I18nContext.set(it, Locale.FRANCE)
            assertThat(I18nContext.getLocale()).isEqualTo(Locale.FRANCE)
        }
        assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)

        val success = AtomicBoolean()
        thread(start = true) {
            assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)
            GlobalEcosContext.newScope(data).use {
                assertThat(I18nContext.getLocale()).isEqualTo(Locale.FRANCE)
            }
            assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)
            success.set(true)
        }.join()

        assertThat(success.get()).isTrue()
    }

    @Test
    fun test() {

        assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)

        val result = I18nContext.doWithLocale(Locale.CANADA) {
            assertThat(I18nContext.getLocale()).isEqualTo(Locale.CANADA)
            assertThat(I18nContext.getLocales()).containsExactly(Locale.CANADA)
            "123"
        }
        assertThat(result).isEqualTo("123")
        assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)
        assertThat(I18nContext.getLocales()).containsExactly(Locale.ENGLISH)
    }

    @Test
    fun customComponentTest() {

        var locale = I18nContext.RUSSIAN
        val prevComponent = I18nContext.component
        I18nContext.component = object : I18nComponent {
            override fun <T> doWithLocales(locales: List<Locale>, action: () -> T): T {
                val prev = locale
                locale = locales[0]
                try {
                    return action.invoke()
                } finally {
                    locale = prev
                }
            }
            override fun getLocales(): List<Locale> {
                return listOf(locale)
            }
            override fun setLocales(locales: List<Locale>) {
                locale = locales.firstOrNull() ?: I18nContext.ENGLISH
            }
        }

        try {
            I18nContext.doWithLocale(Locale.CANADA) {
                assertThat(locale).isEqualTo(Locale.CANADA)
            }
            assertThat(locale).isEqualTo(I18nContext.RUSSIAN)
        } finally {
            I18nContext.component = prevComponent
        }
    }

    @Test
    fun testDefaultLocale() {
        assertThat(I18nContext.getDefaultLocale()).isEqualTo(Locale.ENGLISH)
    }
}
