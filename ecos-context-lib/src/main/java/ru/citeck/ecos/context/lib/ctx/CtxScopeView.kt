package ru.citeck.ecos.context.lib.ctx

interface CtxScopeView {

    operator fun get(key: Any): Any?
}
