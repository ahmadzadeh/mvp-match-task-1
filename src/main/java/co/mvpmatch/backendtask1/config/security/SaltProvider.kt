package co.mvpmatch.backendtask1.config.security

import org.springframework.stereotype.Component

@Component
interface SaltProvider {
    fun getSalt(principal: String): String?
}
