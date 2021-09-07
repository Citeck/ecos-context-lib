package ru.citeck.ecos.context.lib.func

import kotlin.jvm.Throws

interface UncheckedRunnable {

    @Throws(Exception::class)
    fun invoke()
}
