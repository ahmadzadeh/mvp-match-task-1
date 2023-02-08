package co.mvpmatch.backendtask1

import co.mvpmatch.backendtask1.config.ApplicationProperties
import co.mvpmatch.backendtask1.config.security.TokenProvider
import co.mvpmatch.backendtask1.redis.service.RedisPublisher
import co.mvpmatch.backendtask1.vm.SessionEventPayload
import co.mvpmatch.backendtask1.web.api.model.LoginResponseDTO
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZonedDateTime

@Service
class AuthenticationService(
    private val authenticationProvider: AuthenticationProvider,
    private val tokenProvider: TokenProvider,
    private val prop: ApplicationProperties,
    private val redisPublisher: RedisPublisher
) {
    fun authenticate(credentials: UsernamePasswordAuthenticationToken): LoginResponseDTO {
        val authentication: Authentication = authenticationProvider.authenticate(credentials)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication)
        redisPublisher.publish(
            SessionEventPayload(
                username = authentication.name,
                loginAt = Instant.now()
            )
        )
        return LoginResponseDTO().apply {
            token = jwt
            expiresIn = prop.security.tokenValidityInSeconds

        }
    }
}
