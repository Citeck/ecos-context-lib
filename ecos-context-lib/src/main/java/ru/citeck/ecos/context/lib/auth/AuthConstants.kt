package ru.citeck.ecos.context.lib.auth

object AuthConstants {

    const val APP_PREFIX = "APP_"
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
