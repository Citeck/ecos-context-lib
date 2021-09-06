package ru.citeck.ecos.context.lib

import ru.citeck.ecos.context.lib.auth.AuthComponent
import ru.citeck.ecos.context.lib.auth.AuthContext
import ru.citeck.ecos.context.lib.auth.NoopAuthComponent

open class ContextServiceFactory {

    val authComponent: AuthComponent by lazy { createAuthComponent() }

    open fun init() {
        AuthContext.component = authComponent
    }

    protected open fun createAuthComponent(): AuthComponent {
        return NoopAuthComponent()
    }
}
