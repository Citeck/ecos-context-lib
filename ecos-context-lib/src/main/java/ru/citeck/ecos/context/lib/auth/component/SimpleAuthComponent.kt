package ru.citeck.ecos.context.lib.auth.component

import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth
import java.util.concurrent.atomic.AtomicBoolean

class SimpleAuthComponent(private val defaultAuth: AuthData = EmptyAuth) : AuthComponent {

    private val fullAuth = ThreadLocal.withInitial<AuthData> { EmptyAuth }
    private val runAsAuth = ThreadLocal.withInitial<AuthData> { EmptyAuth }
    private val customFullAuth = AtomicBoolean(false)

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {

        if (full) {
            val fullPrev = fullAuth.get()
            val customAuthBefore = customFullAuth.get()
            try {
                customFullAuth.set(true)
                fullAuth.set(auth)
                return action.invoke()
            } finally {
                fullAuth.set(fullPrev)
                customFullAuth.set(customAuthBefore)
            }
        } else {
            if (fullAuth.get().isEmpty()) {
                return runAs(auth, true, action)
            }
            val prevRunAs = runAsAuth.get()
            try {
                runAsAuth.set(auth)
                return action.invoke()
            } finally {
                runAsAuth.set(prevRunAs)
            }
        }
    }

    override fun getCurrentFullAuth(): AuthData {
        if (!customFullAuth.get()) {
            return defaultAuth
        }
        return fullAuth.get()
    }

    override fun getCurrentRunAsAuth(): AuthData {
        val runAs = runAsAuth.get()
        return if (runAs.isEmpty()) {
            getCurrentFullAuth()
        } else {
            runAs
        }
    }

    override fun getSystemAuthorities(): List<String> {
        return emptyList()
    }
}
