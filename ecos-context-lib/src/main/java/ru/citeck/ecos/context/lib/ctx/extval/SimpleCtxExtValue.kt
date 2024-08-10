package ru.citeck.ecos.context.lib.ctx.extval

interface SimpleCtxExtValue<T : Any> : CtxExtValue<T> {

    fun set(value: T?)
}
