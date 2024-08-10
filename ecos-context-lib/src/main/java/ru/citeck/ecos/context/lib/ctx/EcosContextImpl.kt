package ru.citeck.ecos.context.lib.ctx

import io.github.oshai.kotlinlogging.KotlinLogging
import ru.citeck.ecos.context.lib.ctx.extval.CloseableCtxExtValue
import ru.citeck.ecos.context.lib.ctx.extval.CtxExtValue
import ru.citeck.ecos.context.lib.ctx.extval.SimpleCtxExtValue
import java.io.Closeable

class EcosContextImpl(
    private val parent: EcosContextImpl?
) : EcosContext {
    companion object {
        private val EMPTY_SCOPE = object : CtxScopeView {
            override fun get(key: Any): Any? {
                return null
            }
        }

        private val log = KotlinLogging.logger {}
    }

    private val currentScope = ThreadLocal<EcosCtxScope>()
    private val externalValues: MutableMap<Any, CtxExtValue<Any>> = mutableMapOf()

    override fun register(key: Any, threadLocal: ThreadLocal<*>) {
        register(key, ThreadLocalExtValue(threadLocal))
    }

    override fun register(key: Any, value: CtxExtValue<*>) {
        @Suppress("UNCHECKED_CAST")
        val ctxAnyValue = value as CtxExtValue<Any>
        externalValues[key] = ctxAnyValue
    }

    override fun get(key: Any): Any? {
        return currentScope.get()?.get(key)
    }

    override fun getScope(): CtxScopeView {
        return currentScope.get() ?: EMPTY_SCOPE
    }

    override fun getScopeData(): CtxScopeData {

        val extValues = HashMap<Any, Any?>()
        val data = HashMap<Any, Any?>()

        (parent?.getScopeData() as? ScopeDataImpl)?.let {
            extValues.putAll(it.extValues)
            data.putAll(it.data)
        }
        externalValues.forEach { (k, v) ->
            extValues[k] = v.get()
        }
        currentScope.get()?.data?.forEach { (k, v) ->
            data[k] = v
        }
        return if (data.isEmpty() && extValues.isEmpty()) {
            ScopeDataImpl.EMPTY
        } else {
            ScopeDataImpl(extValues, data)
        }
    }

    override fun newScope(): CtxScope {
        val parentScope = currentScope.get()
        val ctxScope = EcosCtxScope(
            parentScope,
            parentScope?.data ?: emptyMap()
        )
        currentScope.set(ctxScope)
        return ctxScope
    }

    /**
     * Creates new scope with provided data.
     */
    override fun newScope(data: CtxScopeData): CtxScope {
        if (data !is ScopeDataImpl) {
            error("Unsupported extracted scope data")
        }
        val ctxScope = EcosCtxScope(currentScope.get(), data.data)
        data.extValues.forEach { (key, value) ->
            ctxScope[key] = value
        }
        currentScope.set(ctxScope)
        return ctxScope
    }

    private fun getExtValue(key: Any): CtxExtValue<Any>? {
        return externalValues[key] ?: parent?.externalValues?.get(key)
    }

    private class ScopeDataImpl(
        val extValues: Map<Any, Any?>,
        val data: Map<Any, Any?>
    ) : CtxScopeData {

        companion object {
            val EMPTY = ScopeDataImpl(emptyMap(), emptyMap())
        }

        override fun <T : Any> get(key: Any): T? {
            if (data.containsKey(key)) {
                @Suppress("UNCHECKED_CAST")
                return data[key] as? T
            } else {
                @Suppress("UNCHECKED_CAST")
                return extValues[key] as? T
            }
        }
    }

    private inner class EcosCtxScope(
        private val parent: EcosCtxScope?,
        parentData: Map<Any, Any?>
    ) : Closeable, CtxScope {

        val data: MutableMap<Any, Any?> = if (parentData.isNotEmpty()) {
            HashMap(parentData)
        } else {
            HashMap(5)
        }
        val extValuesBefore: MutableMap<Any, Any?> = HashMap(5)
        val closeableExtValues = ArrayList<Closeable>()

        override operator fun set(key: Any, value: Any?): EcosCtxScope {
            val extValue = getExtValue(key)
            if (extValue != null) {
                if (extValue is SimpleCtxExtValue<Any>) {
                    if (!extValuesBefore.containsKey(key)) {
                        extValuesBefore[key] = extValue.get()
                    }
                    extValue.set(value)
                } else if (extValue is CloseableCtxExtValue<Any>) {
                    closeableExtValues.add(extValue.set(value))
                }
            } else {
                data[key] = value
            }
            return this
        }

        override operator fun get(key: Any): Any? {
            val extValue = getExtValue(key)
            return if (extValue != null) {
                extValue.get()
            } else {
                if (parent == null || data.containsKey(key)) {
                    data[key]
                } else {
                    parent.data[key]
                }
            }
        }

        override fun close() {
            val currentScopeValue = currentScope.get()
            if (currentScopeValue !== this) {
                log.error { "Some scope was not closed. Unclosed scope data: ${currentScopeValue.data}" }
            }
            if (parent == null) {
                currentScope.remove()
            } else {
                currentScope.set(parent)
            }
            if (extValuesBefore.isNotEmpty()) {
                extValuesBefore.forEach { (k, v) ->
                    try {
                        (getExtValue(k) as? SimpleCtxExtValue<Any>)?.set(v)
                    } catch (e: Throwable) {
                        log.error(e) { "External simple value recovering failed. Key: $k" }
                    }
                }
            }
            for (idx in closeableExtValues.lastIndex downTo 0) {
                try {
                    closeableExtValues[idx].close()
                } catch (e: Throwable) {
                    log.error(e) { "External closeable value closing failed" }
                }
            }
        }
    }

    private class ThreadLocalExtValue<T : Any>(
        val threadLocal: ThreadLocal<T>
    ) : SimpleCtxExtValue<T> {

        override fun get(): T? {
            return threadLocal.get()
        }

        override fun set(value: T?) {
            if (value == null) {
                threadLocal.remove()
            } else {
                threadLocal.set(value)
            }
        }
    }
}
