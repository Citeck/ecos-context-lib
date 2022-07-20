package ru.citeck.ecos.context.lib.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth

class AuthContextTest {

    @Test
    fun runAsSystemRoleTest() {

        assertThat(AuthContext.isRunAsSystem()).isFalse

        AuthContext.runAs("custom-system", listOf(AuthRole.SYSTEM)) {
            assertThat(AuthContext.isRunAsSystem()).isTrue
        }
    }

    @Test
    fun runAsSystemAndAdminTest() {

        assertThat(AuthContext.isRunAsSystem()).isFalse
        assertThat(AuthContext.isRunAsAdmin()).isFalse

        val (isRunAsSystem, isRunAsAdmin) = AuthContext.runAsSystem {
            AuthContext.isRunAsSystem() to AuthContext.isRunAsAdmin()
        }
        assertThat(isRunAsSystem).isTrue
        assertThat(isRunAsAdmin).isFalse

        val (isRunAsSystem2, isRunAsAdmin2) = AuthContext.runAs("admin") {
            AuthContext.isRunAsSystem() to AuthContext.isRunAsAdmin()
        }
        assertThat(isRunAsSystem2).isFalse
        assertThat(isRunAsAdmin2).isFalse

        val (isRunAsSystem3, isRunAsAdmin3) = AuthContext.runAs("admin", listOf(AuthRole.ADMIN)) {
            AuthContext.isRunAsSystem() to AuthContext.isRunAsAdmin()
        }
        assertThat(isRunAsSystem3).isFalse
        assertThat(isRunAsAdmin3).isTrue
    }

    @Test
    fun test() {

        val testUser0 = "test-user-0"
        val testUser1 = "test-user-1"

        expectEmptyAuth()

        AuthContext.runAs(testUser0) {
            assertThat(AuthContext.getCurrentUser()).isEqualTo(testUser0)
            assertThat(AuthContext.getCurrentRunAsUser()).isEqualTo(testUser0)
            AuthContext.runAs(testUser1) {
                assertThat(AuthContext.getCurrentUser()).isEqualTo(testUser0)
                assertThat(AuthContext.getCurrentRunAsUser()).isEqualTo(testUser1)
            }
            assertThat(AuthContext.getCurrentUser()).isEqualTo(testUser0)
            assertThat(AuthContext.getCurrentRunAsUser()).isEqualTo(testUser0)
        }

        expectEmptyAuth()
    }

    private fun expectEmptyAuth() {

        assertThat(AuthContext.getCurrentUserWithAuthorities()).isEmpty()
        assertThat(AuthContext.getCurrentRunAsUserWithAuthorities()).isEmpty()

        assertThat(AuthContext.getCurrentFullAuth()).isSameAs(EmptyAuth)
        assertThat(AuthContext.getCurrentRunAsAuth()).isSameAs(EmptyAuth)

        assertThat(AuthContext.getCurrentUser()).isEmpty()
        assertThat(AuthContext.getCurrentRunAsUser()).isEmpty()
    }
}
