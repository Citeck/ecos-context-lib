package ru.citeck.ecos.context.lib.auth.component

import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class SimpleAuthComponent(defaultAuth: AuthData = EmptyAuth) : AuthComponent {

    private val fullAuth = ThreadLocal.withInitial { defaultAuth }
    private val runAsAuth = ThreadLocal.withInitial { defaultAuth }

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
            if (fullAuth.get() == EmptyAuth) {
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
        return fullAuth.get()
    }

    override fun getCurrentRunAsAuth(): AuthData {
        val runAs = runAsAuth.get()
        return if (runAs == EmptyAuth) {
            fullAuth.get()
        } else {
            runAs
        }
    }

    override fun getSystemAuthorities(): List<String> {
        return emptyList()
    }
}
