package ru.citeck.ecos.context.lib.auth

class NoopAuthComponent : AuthComponent {

    override fun getCurrentUser(): String {
        return ""
    }

    override fun getCurrentAuthorities(): List<String> {
        return emptyList()
    }

    override fun getCurrentRunAsUser(): String {
        return getCurrentUser()
    }

    override fun getCurrentRunAsAuthorities(): List<String> {
        return getCurrentAuthorities()
    }

    override fun <T> runAs(user: String, authorities: List<String>, action: () -> T): T {
        return action.invoke()
    }

    override fun getSystemUser(): String {
        return ""
    }
}
