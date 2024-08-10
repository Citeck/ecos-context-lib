package ru.citeck.ecos.context.lib.ctx

interface CtxScopeData {

    operator fun <T : Any> get(key: Any): T?
}
