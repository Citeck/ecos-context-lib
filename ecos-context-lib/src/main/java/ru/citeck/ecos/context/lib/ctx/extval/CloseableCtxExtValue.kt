package ru.citeck.ecos.context.lib.ctx.extval

import java.io.Closeable

interface CloseableCtxExtValue<T : Any> : CtxExtValue<T> {

    fun set(value: T?): Closeable
}
