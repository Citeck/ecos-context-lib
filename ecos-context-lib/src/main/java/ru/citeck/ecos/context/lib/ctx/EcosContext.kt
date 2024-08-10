package ru.citeck.ecos.context.lib.ctx

import ru.citeck.ecos.context.lib.ctx.extval.CtxExtValue

/**
 * EcosContext Interface
 *
 * This interface is designed to store and manage contextual information such as locale,
 * authorization info, timezone, etc. It allows for the propagation and restoration of
 * contextual data across different scopes and threads.
 *
 * Usage Example:
 *
 * Java:
 * ```java
 * try (CtxScope scope = ecosContext.newScope()) {
 *   scope.set("key", "value");
 * }
 * ```
 *
 * Kotlin:
 * ```kotlin
 * ecosContext.newScope().use { scope ->
 *   scope.set("key", "value")
 * }
 * ```
 */
interface EcosContext {

    /**
     * Registers a `ThreadLocal` variable with a given key.
     * The value stored in this `ThreadLocal` will be extracted when `getScopeData()`
     * is called and restored after `newScope(data: CtxScopeData)` is invoked.
     *
     * @param key - The key associated with the `ThreadLocal` variable.
     * @param threadLocal - The `ThreadLocal` variable to be registered.
     */
    fun register(key: Any, threadLocal: ThreadLocal<*>)

    /**
     * Registers an external custom contextual value with the given key.
     * The value will be extracted when `getScopeData()` is called and restored
     * after `newScope(data: CtxScopeData)` is invoked.
     *
     * @param key - The key associated with the custom contextual value.
     * @param value - The external contextual value to be registered.
     */
    fun register(key: Any, value: CtxExtValue<*>)

    /**
     * Retrieves the contextual data associated with the specified key.
     *
     * @param key - The key for which to retrieve the contextual data.
     * @return - The contextual data associated with the key, or `null` if not found.
     */
    fun get(key: Any): Any?

    /**
     * Retrieves the current read-only scope.
     * This can be used to extract the contextual data.
     *
     * @return - The current scope view containing contextual data.
     */
    fun getScope(): CtxScopeView

    /**
     * Extracts all contextual data, enabling it to be used in another context or thread.
     * This method can be used in conjunction with `newScope(data: CtxScopeData)` to
     * propagate contextual data across threads.
     *
     * @return - A `CtxScopeData` object containing all the current contextual data.
     */
    fun getScopeData(): CtxScopeData

    /**
     * Creates a new context scope. Scopes are utilized to manage a subset of
     * contextual data within a specific lifecycle.
     *
     * @return - A new `CtxScope` instance.
     */
    fun newScope(): CtxScope

    /**
     * Creates a new context scope with the provided contextual data.
     * This method is typically used to restore a context in a different thread
     * with the data retrieved using `getScopeData()`.
     *
     * @param data - The contextual data to be used for the new scope.
     * @return - A new `CtxScope` instance initialized with the provided data.
     */
    fun newScope(data: CtxScopeData): CtxScope
}
