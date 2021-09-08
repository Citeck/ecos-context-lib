package ru.citeck.ecos.context.lib.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class AuthContextTest {

    @Test
    fun test() {

        val testUser0 = "test-user-0"
        val testUser1 = "test-user-1"

        expectEmptyAuth()

        AuthContext.runAs(testUser0) {
            assertThat(AuthContext.getCurrentUser()).isEqualTo(testUser0)
            assertThat(AuthContext.getCurrentRunAsUser()).isEqualTo(testUser0)
            AuthContext.runAs(testUser1) {
                assertThat(AuthContext.getCurrentUser()).isEqualTo(testUser1)
                assertThat(AuthContext.getCurrentRunAsUser()).isEqualTo(testUser1)
            }
            assertThat(AuthContext.getCurrentUser()).isEqualTo(testUser0)
            assertThat(AuthContext.getCurrentRunAsUser()).isEqualTo(testUser0)
        }

        expectEmptyAuth()
    }

    private fun expectEmptyAuth() {

        assertThat(AuthContext.getCurrentFullAuth()).isSameAs(EmptyAuth)
        assertThat(AuthContext.getCurrentRunAsAuth()).isSameAs(EmptyAuth)

        assertThat(AuthContext.getCurrentUser()).isEmpty()
        assertThat(AuthContext.getCurrentRunAsUser()).isEmpty()
    }
}
