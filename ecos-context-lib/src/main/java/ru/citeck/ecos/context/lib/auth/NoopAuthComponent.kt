package ru.citeck.ecos.context.lib.auth

class NoopAuthComponent : AuthComponent {

    override fun getCurrentUser(): String {
        return ""
    }

    override fun getCurrentUserAuthorities(): List<String> {
        return emptyList()
    }

    override fun getCurrentRunAsUser(): String {
        return getCurrentUser()
    }

    override fun getCurrentRunAsUserAuthorities(): List<String> {
        return getCurrentUserAuthorities()
    }

    override fun <T> runAs(user: String, authorities: List<String>, action: () -> T): T {
        return action.invoke()
    }

    override fun getSystemUser(): String {
        return ""
    }
}
