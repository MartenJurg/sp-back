package ee.developest.studentpraxis.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "app.jwt")
class JwtConfig {
    fun getDurationMillis(): Long {
        return (duration ?: 0) * 60 * 1000
    }

    var secret: String? = null
    var duration: Long? = null
}