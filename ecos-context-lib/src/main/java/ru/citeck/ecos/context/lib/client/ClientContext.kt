package ru.citeck.ecos.context.lib.client

import ru.citeck.ecos.context.lib.client.component.ClientComponent
import ru.citeck.ecos.context.lib.client.component.SimpleClientComponent
import ru.citeck.ecos.context.lib.client.data.ClientData
import ru.citeck.ecos.context.lib.ctx.CtxScope
import ru.citeck.ecos.context.lib.ctx.GlobalEcosContext
import ru.citeck.ecos.context.lib.ctx.extval.SimpleCtxExtValue
import ru.citeck.ecos.context.lib.func.UncheckedRunnable
import ru.citeck.ecos.context.lib.func.UncheckedSupplier

object ClientContext {

    var component: ClientComponent = SimpleClientComponent()

    init {
        GlobalEcosContext.register(
            ClientContext::class,
            object : SimpleCtxExtValue<ClientData> {
                override fun get(): ClientData {
                    return component.getClientData()
                }
                override fun set(value: ClientData?) {
                    return component.setClientData(value ?: SimpleClientComponent.DEFAULT)
                }
            }
        )
    }

    fun set(scope: CtxScope, clientData: ClientData) {
        scope[ClientContext::class] = clientData
    }

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
