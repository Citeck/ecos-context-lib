package ru.citeck.ecos.context.lib.auth

import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class SimpleAuthComponent : AuthComponent {

    private val currentAuth = ThreadLocal.withInitial<AuthData> { EmptyAuth }

    override fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T {
        val prevInfo = currentAuth.get()
        try {
            currentAuth.set(auth)
            return action.invoke()
        } finally {
            currentAuth.set(prevInfo)
        }
    }

    override fun getCurrentFullAuth(): AuthData {
        return currentAuth.get()
    }

    override fun getCurrentRunAsAuth(): AuthData {
        return getCurrentFullAuth()
    }

    override fun getSystemAuthorities(): List<String> {
        return emptyList()
    }
}
