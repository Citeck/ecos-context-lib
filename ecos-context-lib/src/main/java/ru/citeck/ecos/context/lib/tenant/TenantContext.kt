package ru.citeck.ecos.context.lib.tenant

object TenantContext {

    const val DEFAULT_TENANT = ""

    private val current = ThreadLocal.withInitial { DEFAULT_TENANT }

    fun <T> runInTenant(tenant: String, action: () -> T): T {
        val prev = current.get()
        try {
            current.set(tenant)
            return action.invoke()
        } finally {
            current.set(prev)
        }
    }

    fun getCurrent(): String {
        return current.get()
    }
}
