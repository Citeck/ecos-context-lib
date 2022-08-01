package ru.citeck.ecos.context.lib.client.data

data class ClientData(
    val ip: String
) {
    companion object {
        @JvmField
        val EMPTY = create {}

        @JvmStatic
        fun create(): Builder {
            return Builder()
        }

        @JvmStatic
        fun create(builder: Builder.() -> Unit): ClientData {
            val builderObj = Builder()
            builder.invoke(builderObj)
            return builderObj.build()
        }
    }

    class Builder() {

        var ip: String = ""

        constructor(other: ClientData) : this() {
            this.ip = other.ip
        }

        fun withIp(ip: String?): Builder {
            this.ip = ip ?: EMPTY.ip
            return this
        }

        fun build(): ClientData {
            return ClientData(ip)
        }
    }
}
