package ru.citeck.ecos.context.lib.time.component

import java.time.Duration

interface TimeZoneComponent {

    fun setUtcOffset(offset: Duration)

    fun <T> doWithUtcOffset(offset: Duration, action: () -> T): T

    fun getUtcOffset(): Duration
}
