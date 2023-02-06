package co.mvpmatch.backendtask1.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
class ApplicationProperties {
    var security = Security()

    class Security {
        lateinit var secret: String
        var tokenValidityInSeconds: Long = 0
    }
}
