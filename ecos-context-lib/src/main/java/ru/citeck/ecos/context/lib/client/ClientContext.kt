package ru.citeck.ecos.context.lib.client

import ru.citeck.ecos.context.lib.client.component.ClientComponent
import ru.citeck.ecos.context.lib.client.component.SimpleClientComponent
import ru.citeck.ecos.context.lib.client.data.ClientData

object ClientContext {

    var component: ClientComponent = SimpleClientComponent()

    @JvmStatic
    fun <T> doWithClientData(clientData: ClientData, action: () -> T): T {
        return component.doWithClientData(clientData, action)
    }

    @JvmStatic
    fun getClientData(): ClientData {
        return component.getClientData()
    }
}
