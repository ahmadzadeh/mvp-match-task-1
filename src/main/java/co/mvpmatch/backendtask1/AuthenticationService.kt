package co.mvpmatch.backendtask1

import co.mvpmatch.backendtask1.config.ApplicationProperties
import co.mvpmatch.backendtask1.config.ResourceNotFoundException
import co.mvpmatch.backendtask1.config.security.TokenProvider
import co.mvpmatch.backendtask1.redis.repository.SessionRepository
import co.mvpmatch.backendtask1.redis.service.SessionEventPublisher
import co.mvpmatch.backendtask1.service.UserService
import co.mvpmatch.backendtask1.vm.LoginLogout
import co.mvpmatch.backendtask1.vm.SessionEventPayload
import co.mvpmatch.backendtask1.web.api.model.LoginResponseDTO
import co.mvpmatch.backendtask1.web.api.model.SessionDTO
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class AuthenticationService(
    private val authenticationProvider: AuthenticationProvider,
    private val tokenProvider: TokenProvider,
    private val prop: ApplicationProperties,
    private val sessionEventPublisher: SessionEventPublisher,
    private val sessionRepository: SessionRepository
) {
    fun authenticate(credentials: UsernamePasswordAuthenticationToken): LoginResponseDTO {
        val authentication: Authentication = authenticationProvider.authenticate(credentials)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication)

        val currentSessions = getSessions(authentication.name)
        publishLoginEvent(authentication.name)

        return LoginResponseDTO().apply {
            token = jwt
            expiresIn = prop.security.tokenValidityInSeconds
            previousSessions = currentSessions
        }
    }

    @Throws(ResourceNotFoundException::class)
    fun terminateUserSessions(userName: String) {
        sessionEventPublisher.publish(
            SessionEventPayload(
                actionType = LoginLogout.LOGOUT,
                username = userName,
                timestamp = Instant.now()
            )

        )
    }

    private fun publishLoginEvent(userName: String) {
        sessionEventPublisher.publish(
            SessionEventPayload(
                actionType = LoginLogout.LOGIN,
                username = userName,
                timestamp = Instant.now()
            )
        )
    }

    private fun getSessions(userName: String): List<SessionDTO> {
        return sessionRepository.findAllByUsername(userName)
            .map {
                SessionDTO().apply {
                    sessionId = it.id
                    createdAt = it.createdAt.toOffsetDateTime()
                }
            }
    }
}
