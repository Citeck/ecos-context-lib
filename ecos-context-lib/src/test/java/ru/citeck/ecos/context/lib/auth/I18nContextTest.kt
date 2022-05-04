package ru.citeck.ecos.context.lib.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.i18n.I18nContext
import ru.citeck.ecos.context.lib.i18n.component.I18nComponent
import java.util.*

class I18nContextTest {

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
