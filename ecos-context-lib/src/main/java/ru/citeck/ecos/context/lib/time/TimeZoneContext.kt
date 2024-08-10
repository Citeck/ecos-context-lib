package ru.citeck.ecos.context.lib.time

import ru.citeck.ecos.context.lib.ctx.CtxScope
import ru.citeck.ecos.context.lib.ctx.GlobalEcosContext
import ru.citeck.ecos.context.lib.ctx.extval.SimpleCtxExtValue
import ru.citeck.ecos.context.lib.func.UncheckedRunnable
import ru.citeck.ecos.context.lib.func.UncheckedSupplier
import ru.citeck.ecos.context.lib.time.component.SimpleTimeZoneComponent
import ru.citeck.ecos.context.lib.time.component.TimeZoneComponent
import java.time.Duration

object TimeZoneContext {

    var component: TimeZoneComponent = SimpleTimeZoneComponent()

    init {
        GlobalEcosContext.register(
            TimeZoneContext::class,
            object : SimpleCtxExtValue<Duration> {
                override fun get(): Duration {
                    return component.getUtcOffset()
                }
                override fun set(value: Duration?) {
                    return component.setUtcOffset(value ?: Duration.ZERO)
                }
            }
        )
    }

    fun set(scope: CtxScope, offset: Duration) {
        scope[TimeZoneContext::class] = offset
    }

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
