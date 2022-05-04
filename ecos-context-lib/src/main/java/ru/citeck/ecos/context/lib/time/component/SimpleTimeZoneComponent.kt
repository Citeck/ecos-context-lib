package ru.citeck.ecos.context.lib.time.component

import java.time.Duration

class SimpleTimeZoneComponent : TimeZoneComponent {

    companion object {
        val DEFAULT = Duration.ZERO
    }

    private val current = ThreadLocal.withInitial { DEFAULT }

    override fun <T> doWithUtcOffset(offset: Duration, action: () -> T): T {
        val prevValue = this.current.get()
        try {
            this.current.set(offset)
            return action.invoke()
        } finally {
            this.current.set(prevValue)
        }
    }

    override fun getUtcOffset(): Duration {
        return current.get()
    }
}
