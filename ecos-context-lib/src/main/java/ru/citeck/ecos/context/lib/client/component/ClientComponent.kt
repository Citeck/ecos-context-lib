package ru.citeck.ecos.context.lib.client.component

import ru.citeck.ecos.context.lib.client.data.ClientData

interface ClientComponent {

    fun setClientData(clientData: ClientData)

    fun <T> doWithClientData(clientData: ClientData, action: () -> T): T

    fun getClientData(): ClientData
}
