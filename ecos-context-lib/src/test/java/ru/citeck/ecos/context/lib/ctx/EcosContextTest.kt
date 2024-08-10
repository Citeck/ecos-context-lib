package ru.citeck.ecos.context.lib.ctx

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.auth.AuthContext
import ru.citeck.ecos.context.lib.auth.AuthRole
import ru.citeck.ecos.context.lib.auth.data.EmptyAuth
import ru.citeck.ecos.context.lib.client.ClientContext
import ru.citeck.ecos.context.lib.client.data.ClientData
import ru.citeck.ecos.context.lib.i18n.I18nContext
import ru.citeck.ecos.context.lib.time.TimeZoneContext
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.thread

class EcosContextTest {

    @Test
    fun test2() {

        val scopeData = GlobalEcosContext.newScope().use {
            I18nContext.doWithLocale(Locale.CANADA) {
                ClientContext.doWithClientData(ClientData("1.1.1.1")) {
                    AuthContext.runAs("user0", listOf("auth0", "auth1")) {
                        AuthContext.runAsSystem {
                            TimeZoneContext.doWithUtcOffset(Duration.ofMinutes(100)) {
                                GlobalEcosContext.getScopeData()
                            }
                        }
                    }
                }
            }
        }
        println(scopeData)

        fun expectDefaultContext() {
            assertThat(I18nContext.getLocale()).isEqualTo(Locale.ENGLISH)
            assertThat(AuthContext.getCurrentRunAsAuth()).isEqualTo(EmptyAuth)
            assertThat(AuthContext.getCurrentFullAuth()).isEqualTo(EmptyAuth)
            assertThat(ClientContext.getClientData()).isEqualTo(ClientData.EMPTY)
            assertThat(TimeZoneContext.getUtcOffset()).isEqualTo(Duration.ZERO)
        }

        fun assertScope() {
            expectDefaultContext()
            GlobalEcosContext.newScope(scopeData).use {
                assertThat(I18nContext.getLocale()).isEqualTo(Locale.CANADA)
                assertThat(AuthContext.getCurrentUser()).isEqualTo("user0")
                assertThat(AuthContext.getCurrentAuthorities()).isEqualTo(listOf("auth0", "auth1"))
                assertThat(AuthContext.getCurrentRunAsUser()).isEqualTo("system")
                assertThat(AuthContext.getCurrentRunAsAuthorities()).contains(AuthRole.SYSTEM)
                assertThat(ClientContext.getClientData()).isEqualTo(ClientData("1.1.1.1"))
                assertThat(TimeZoneContext.getUtcOffset()).isEqualTo(Duration.ofMinutes(100))
            }
            expectDefaultContext()
        }

        assertScope()

        val success = AtomicBoolean()
        thread(start = true) {
            assertScope()
            success.set(true)
        }.join()

        assertThat(success.get()).isTrue()
    }

    @Test
    fun test() {

        val key = "key"
        assertThat(GlobalEcosContext.get(key)).isNull()

        val extractedScope = AtomicReference<CtxScopeData>()

        GlobalEcosContext.newScope().use { scope0 ->
            scope0[key] = "bbb"
            assertThat(GlobalEcosContext.get(key)).isEqualTo("bbb")
            GlobalEcosContext.newScope().use { scope1 ->
                scope1[key] = "ccc"
                assertThat(GlobalEcosContext.get(key)).isEqualTo("ccc")
                GlobalEcosContext.newScope().use { scope2 ->
                    scope2[key] = null
                    assertThat(GlobalEcosContext.get(key)).isNull()
                }
                extractedScope.set(GlobalEcosContext.getScopeData())
                assertThat(GlobalEcosContext.get(key)).isEqualTo("ccc")
            }
            assertThat(GlobalEcosContext.get(key)).isEqualTo("bbb")
        }

        assertThat(GlobalEcosContext.get(key)).isNull()

        GlobalEcosContext.newScope(extractedScope.get()).use { scope0 ->
            assertThat(GlobalEcosContext.get(key)).isEqualTo("ccc")
        }

        assertThat(GlobalEcosContext.get(key)).isNull()
    }
}
