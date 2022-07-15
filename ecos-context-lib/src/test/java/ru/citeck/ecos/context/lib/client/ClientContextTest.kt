package ru.citeck.ecos.context.lib.client

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.client.data.ClientData

internal class ClientContextTest {

    @Test
    fun testDefaultValue() {
        assertEquals(ClientData.EMPTY, ClientContext.getClientData())
    }

    @Test
    fun doWithClientData() {
        val dataBefore = ClientContext.getClientData()

        val data = ClientData("0.0.0.0")
        ClientContext.doWithClientData(data) {
            assertEquals(data, ClientContext.getClientData())
        }

        assertEquals(dataBefore, ClientContext.getClientData())
    }
}
