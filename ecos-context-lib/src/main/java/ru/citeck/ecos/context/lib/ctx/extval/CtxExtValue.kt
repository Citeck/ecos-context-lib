package ru.citeck.ecos.context.lib.ctx.extval

interface CtxExtValue<T : Any> {

    fun get(): T?
}
