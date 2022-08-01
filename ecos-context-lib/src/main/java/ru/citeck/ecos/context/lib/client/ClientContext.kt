package ru.citeck.ecos.context.lib.client

import ru.citeck.ecos.context.lib.client.component.ClientComponent
import ru.citeck.ecos.context.lib.client.component.SimpleClientComponent
import ru.citeck.ecos.context.lib.client.data.ClientData
import ru.citeck.ecos.context.lib.func.UncheckedRunnable
import ru.citeck.ecos.context.lib.func.UncheckedSupplier

object ClientContext {

    var component: ClientComponent = SimpleClientComponent()

    @JvmStatic
    fun doWithClientDataJ(clientData: ClientData, action: UncheckedRunnable) {
        doWithClientData(clientData) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithClientDataJ(clientData: ClientData, action: UncheckedSupplier<T>): T {
        return doWithClientData(clientData) { action.invoke() }
    }

    @JvmStatic
    fun <T> doWithClientData(clientData: ClientData, action: () -> T): T {
        return component.doWithClientData(clientData, action)
    }

    @JvmStatic
    fun getClientData(): ClientData {
        return component.getClientData()
    }
}
