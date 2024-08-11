package ru.citeck.ecos.context.lib.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.auth.component.DefaultAuthComponent
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth
import ru.citeck.ecos.context.lib.auth.data.SimpleAuthData
import ru.citeck.ecos.context.lib.auth.data.TokenAuthData

class AuthContextTest {

    @Test
    fun runAsEmptyTest() {
        assertThat(AuthContext.getCurrentRunAsAuth()).isEqualTo(EmptyAuth)
        val componentBefore = AuthContext.component
        try {
            AuthContext.runAsSystem {
                AuthContext.component = DefaultAuthComponent(AuthContext.SYSTEM_AUTH)
            }
            fun assertUsers(full: String, runAs: String) {
                assertThat(AuthContext.getCurrentFullAuth().getUser()).isEqualTo(full)
                assertThat(AuthContext.getCurrentRunAsAuth().getUser()).isEqualTo(runAs)
            }
            assertUsers(AuthUser.SYSTEM, AuthUser.SYSTEM)

            AuthContext.runAs("user") {
                assertUsers("user", "user")
                AuthContext.runAs("user2") {
                    assertUsers("user", "user2")
                    AuthContext.runAs("user3") {
                        assertUsers("user", "user3")
                        AuthContext.runAs(EmptyAuth) {
                            assertUsers("", "")
                        }
                        AuthContext.runAsFull(EmptyAuth) {
                            assertUsers("", "")
                        }
                        assertUsers("user", "user3")
                    }
                    assertUsers("user", "user2")
                }
                assertUsers("user", "user")
            }

            assertUsers(AuthUser.SYSTEM, AuthUser.SYSTEM)

            AuthContext.runAs(EmptyAuth) {
                assertUsers("", "")
            }

            AuthContext.runAsFull(EmptyAuth) {
                assertUsers("", "")
            }
        } finally {
            AuthContext.runAsSystem {
                AuthContext.component = componentBefore
            }
        }
    }

    @Test
    fun fullAuthWithRunAsTest() {
        AuthContext.runAs("abc") {
            assertThat(AuthContext.getCurrentUser()).isEqualTo("abc")
            assertThat(AuthContext.getCurrentFullAuth().getUser()).isEqualTo("abc")
            assertThat(AuthContext.getCurrentRunAsAuth().getUser()).isEqualTo("abc")
            AuthContext.runAs("def") {
                assertThat(AuthContext.getCurrentUser()).isEqualTo("abc")
                assertThat(AuthContext.getCurrentFullAuth().getUser()).isEqualTo("abc")
                assertThat(AuthContext.getCurrentRunAsAuth().getUser()).isEqualTo("def")
                AuthContext.runAsFull("hij") {
                    assertThat(AuthContext.getCurrentUser()).isEqualTo("hij")
                    assertThat(AuthContext.getCurrentFullAuth().getUser()).isEqualTo("hij")
                    assertThat(AuthContext.getCurrentRunAsAuth().getUser()).isEqualTo("hij")
                }
            }
        }
    }

    @Test
    fun authToStringTest() {
        val auth = SimpleAuthData(
            "user",
            listOf("auth0", "auth1", "aut\"h2")
        )
        assertThat(auth.toString()).isEqualTo(
            "{\"user\":\"user\",\"authorities\":[\"auth0\",\"auth1\",\"aut\\\"h2\"]}"
        )
        val tokenAuth = TokenAuthData(
            "user",
            listOf("auth0", "auth1", "aut\"h2"),
            "tokenAb\"C"
        )
        assertThat(tokenAuth.toString()).isEqualTo(
            "{\"user\":\"user\",\"authorities\":[\"auth0\",\"auth1\",\"aut\\\"h2\"],\"token\":\"tokenAb\\\"C\"}"
        )
    }

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
