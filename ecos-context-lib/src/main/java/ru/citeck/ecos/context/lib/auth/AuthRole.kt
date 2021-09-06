package ru.citeck.ecos.context.lib.auth

object AuthRole {

    // prefix

    const val GROUP_PREFIX = "GROUP_"
    const val ROLE_PREFIX = "ROLE_"

    // role

    /**
     * System role. Used when AuthContext.runAsSystem {} called
     */
    const val SYSTEM = ROLE_PREFIX + "SYSTEM"

    const val ADMIN = ROLE_PREFIX + "ADMIN"
    /**
     * Authenticated user
     */
    const val USER = ROLE_PREFIX + "USER"

    const val GUEST = ROLE_PREFIX + "GUEST"

    const val ANONYMOUS = ROLE_PREFIX + "ANONYMOUS"
}
