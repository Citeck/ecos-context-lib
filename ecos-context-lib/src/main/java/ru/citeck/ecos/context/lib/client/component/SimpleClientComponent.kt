package ru.citeck.ecos.context.lib.client.component

import ru.citeck.ecos.context.lib.client.data.ClientData

class SimpleClientComponent : ClientComponent {

    companion object {
        val DEFAULT = ClientData.EMPTY
    }

    private val current = ThreadLocal.withInitial { DEFAULT }

    override fun <T> doWithClientData(clientData: ClientData, action: () -> T): T {
        val prevValue = this.current.get()
        try {
            this.current.set(clientData)
            return action.invoke()
        } finally {
            this.current.set(prevValue)
        }
    }

    override fun getClientData(): ClientData {
        return current.get()
    }
}
