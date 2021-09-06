package ru.citeck.ecos.context.lib

import ru.citeck.ecos.context.lib.auth.AuthComponent
import ru.citeck.ecos.context.lib.auth.AuthContext

open class ContextServiceFactory {

    open fun init() {
        AuthContext.component = createAuthComponent() ?: AuthContext.component
    }

    protected open fun createAuthComponent(): AuthComponent? {
        return null
    }
}
