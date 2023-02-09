package ru.citeck.ecos.context.lib.auth.data

import java.util.*

abstract class AbstractAuthData : AuthData {

    override fun isEmpty(): Boolean {
        return getUser().isEmpty() && getAuthorities().isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is AuthData) {
            return false
        }
        if (getUser() != other.getUser()) {
            return false
        }
        val currentAuthorities = getAuthorities()
        val otherAuthorities = other.getAuthorities()
        if (currentAuthorities.size != otherAuthorities.size) {
            return false
        }
        for (i in currentAuthorities.indices) {
            if (currentAuthorities[i] != otherAuthorities[i]) {
                return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        return Objects.hash(getUser(), getAuthorities())
    }

    override fun toString(): String {
        return "{" +
            "\"user\":\"${getUser().replace("\"", "\\\"")}\"," +
            "\"authorities\":[" +
            getAuthorities().joinToString(",") {
                "\"" + it.replace("\"", "\\\"") + "\""
            } +
            "]" +
            "}"
    }
}
