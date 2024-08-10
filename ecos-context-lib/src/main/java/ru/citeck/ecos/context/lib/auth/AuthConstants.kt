package ru.citeck.ecos.context.lib.auth

object AuthConstants {

    const val MDC_USER_KEY = "ecosUser"

    const val APP_PREFIX = "APP_"

    @Deprecated(
        "use AuthGroup.PREFIX",
        replaceWith = ReplaceWith(
            "AuthGroup.PREFIX",
            "ru.citeck.ecos.context.lib.auth.AuthGroup"
        )
    )
    const val GROUP_PREFIX = "GROUP_"

    @Deprecated(
        "use AuthUser.SYSTEM",
        replaceWith = ReplaceWith(
            "AuthUser.SYSTEM",
            "ru.citeck.ecos.context.lib.auth.AuthUser"
        )
    )
    const val SYSTEM_USER = AuthUser.SYSTEM
}
