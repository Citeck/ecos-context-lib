package ru.citeck.ecos.context.lib.ctx

import ru.citeck.ecos.context.lib.ctx.extval.CtxExtValue

object GlobalEcosContext {

    private val globalContext = EcosContextImpl(null)

    @JvmStatic
    fun createChild(): EcosContext {
        return EcosContextImpl(globalContext)
    }

    @JvmStatic
    fun getContext(): EcosContext {
        return globalContext
    }

    @JvmStatic
    fun register(key: Any, threadLocal: ThreadLocal<*>) {
        globalContext.register(key, threadLocal)
    }

    @JvmStatic
    fun register(key: Any, value: CtxExtValue<*>) {
        globalContext.register(key, value)
    }

    @JvmStatic
    fun get(key: Any): Any? {
        return globalContext.get(key)
    }

    @JvmStatic
    fun getScope(): CtxScopeView {
        return globalContext.getScope()
    }

    @JvmStatic
    fun getScopeData(): CtxScopeData {
        return globalContext.getScopeData()
    }

    @JvmStatic
    fun newScope(): CtxScope {
        return globalContext.newScope()
    }

    @JvmStatic
    fun newScope(data: CtxScopeData): CtxScope {
        return globalContext.newScope(data)
    }
}
