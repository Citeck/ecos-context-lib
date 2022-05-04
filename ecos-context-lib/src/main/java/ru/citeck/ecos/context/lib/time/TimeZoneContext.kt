package ru.citeck.ecos.context.lib.time

import ru.citeck.ecos.context.lib.func.UncheckedRunnable
import ru.citeck.ecos.context.lib.func.UncheckedSupplier
import ru.citeck.ecos.context.lib.time.component.SimpleTimeZoneComponent
import ru.citeck.ecos.context.lib.time.component.TimeZoneComponent
import java.time.Duration

object TimeZoneContext {

    var component: TimeZoneComponent = SimpleTimeZoneComponent()

    @JvmStatic
    fun doWithUtcOffsetJ(offset: Duration?, action: UncheckedRunnable) {
        return doWithUtcOffset(offset) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithUtcOffsetJ(offset: Duration?, action: UncheckedSupplier<T>): T {
        return doWithUtcOffset(offset) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithUtcOffset(offset: Duration?, action: () -> T): T {
        return component.doWithUtcOffset(offset ?: Duration.ZERO, action)
    }

    @JvmStatic
    fun getUtcOffset(): Duration {
        return component.getUtcOffset()
    }
}
