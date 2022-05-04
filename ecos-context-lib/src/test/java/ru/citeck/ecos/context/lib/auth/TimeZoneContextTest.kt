package ru.citeck.ecos.context.lib.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import ru.citeck.ecos.context.lib.time.TimeZoneContext
import java.time.Duration

class TimeZoneContextTest {

    @Test
    fun test() {

        assertThat(TimeZoneContext.getUtcOffset()).isEqualTo(Duration.ZERO)

        val result = TimeZoneContext.doWithUtcOffset(Duration.ofHours(10)) {
            assertThat(TimeZoneContext.getUtcOffset()).isEqualTo(Duration.ofHours(10))
            "123"
        }
        assertThat(result).isEqualTo("123")
        assertThat(TimeZoneContext.getUtcOffset()).isEqualTo(Duration.ZERO)
    }
}
