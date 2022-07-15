package ru.citeck.ecos.context.lib.client.data

data class ClientData(
    val ip: String
) {
    companion object {
        val EMPTY = ClientData("")
    }
}
