package ru.citeck.ecos.context.lib.auth.component

import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class SimpleAuthComponent(private val defaultAuth: AuthData = EmptyAuth) : AuthComponent {

    private val fullAuth = ThreadLocal.withInitial<AuthData> { EmptyAuth }
    private val runAsAuth = ThreadLocal.withInitial<AuthData> { EmptyAuth }

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {

        if (full) {
            val fullPrev = fullAuth.get()
            try {
                fullAuth.set(auth)
                return action.invoke()
            } finally {
                fullAuth.set(fullPrev)
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
        val fullAuth = fullAuth.get()
        return if (fullAuth.isEmpty()) {
            defaultAuth
        } else {
            fullAuth
        }
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
