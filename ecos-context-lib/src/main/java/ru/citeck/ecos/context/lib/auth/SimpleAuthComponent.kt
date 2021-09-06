package ru.citeck.ecos.context.lib.auth

class SimpleAuthComponent : AuthComponent {

    private val currentAuth = ThreadLocal.withInitial<AuthData> { SimpleAuthData.EMPTY }

    override fun <T> runAs(auth: AuthData, action: () -> T): T {
        val prevInfo = currentAuth.get()
        try {
            val newInfo = if (!auth.getAuthorities().contains(auth.getUser())) {
                val newAuthorities = ArrayList<String>()
                newAuthorities.add(auth.getUser())
                newAuthorities.addAll(auth.getAuthorities())
                SimpleAuthData(auth.getUser(), newAuthorities)
            } else {
                auth
            }
            currentAuth.set(newInfo)
            return action.invoke()
        } finally {
            currentAuth.set(prevInfo)
        }
    }

    override fun getCurrentAuth(): AuthData {
        return currentAuth.get()
    }

    override fun getCurrentRunAsAuth(): AuthData {
        return getCurrentAuth()
    }

    override fun getSystemUser(): String {
        return "system"
    }
}
