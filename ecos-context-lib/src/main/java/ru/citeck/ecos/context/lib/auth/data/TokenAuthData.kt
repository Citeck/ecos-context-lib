package ru.citeck.ecos.context.lib.auth.data

open class TokenAuthData(
    user: String,
    authorities: List<String>,
    private val token: String
) : SimpleAuthData(user, authorities) {

    fun getToken(): String {
        return token
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is TokenAuthData) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }
        return other.token == this.token
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + token.hashCode()
        return result
    }

    override fun toString(): String {
        var result = super.toString()
        result = result.substring(0, result.length - 1)
        result += ",\"token\":\"" + token.replace("\"", "\\\"") + "\"}"
        return result
    }
}
