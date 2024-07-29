package ru.citeck.ecos.context.lib.auth

object AuthRole {

    // prefix

    const val PREFIX = "ROLE_"

    // role

    const val ADMIN = PREFIX + "ADMIN"

    /**
     * Authenticated user
     */
    const val USER = PREFIX + "USER"
    const val GUEST = PREFIX + "GUEST"
    const val SYSTEM = PREFIX + "SYSTEM"
    const val ANONYMOUS = PREFIX + "ANONYMOUS"
}
