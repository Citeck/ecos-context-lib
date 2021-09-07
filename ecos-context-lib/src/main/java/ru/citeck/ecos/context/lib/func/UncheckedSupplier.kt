package ru.citeck.ecos.context.lib.func

import kotlin.jvm.Throws

interface UncheckedSupplier<T> {

    @Throws(Exception::class)
    fun invoke(): T
}
