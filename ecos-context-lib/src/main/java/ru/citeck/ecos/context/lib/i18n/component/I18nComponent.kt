package ru.citeck.ecos.context.lib.i18n.component

import java.util.*

interface I18nComponent {

    fun <T> doWithLocales(locales: List<Locale>, action: () -> T): T

    fun getLocales(): List<Locale>

    fun setLocales(locales: List<Locale>)
}
