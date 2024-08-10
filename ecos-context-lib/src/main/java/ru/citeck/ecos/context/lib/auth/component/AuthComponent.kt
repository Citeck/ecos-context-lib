package ru.citeck.ecos.context.lib.auth.component

import ru.citeck.ecos.context.lib.auth.data.AuthData
import ru.citeck.ecos.context.lib.auth.data.AuthState

interface AuthComponent {

    fun setAuthState(authState: AuthState)

    fun getAuthState(): AuthState

    fun getCurrentFullAuth(): AuthData

    fun getCurrentRunAsAuth(): AuthData

    fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T

    fun getSystemAuthorities(): List<String>
}
