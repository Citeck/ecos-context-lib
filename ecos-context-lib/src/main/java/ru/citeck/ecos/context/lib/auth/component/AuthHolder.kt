package ru.citeck.ecos.context.lib.auth.component

import ru.citeck.ecos.context.lib.auth.data.AuthData

interface AuthHolder {

    fun set(authData: AuthData)
}
