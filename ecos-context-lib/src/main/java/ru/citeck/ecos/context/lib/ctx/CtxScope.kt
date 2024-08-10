package ru.citeck.ecos.context.lib.ctx

import java.io.Closeable

interface CtxScope : CtxScopeView, Closeable {

    operator fun set(key: Any, value: Any?): CtxScope
}
