package ru.citeck.ecos.context.lib.auth.component

import ru.citeck.ecos.context.lib.auth.data.AuthData

interface AuthComponent {

    fun getCurrentFullAuth(): AuthData

    fun getCurrentRunAsAuth(): AuthData

    fun <T> runAs(auth: AuthData, full: Boolean, action: () -> T): T

    fun getSystemAuthorities(): List<String>
}
