package co.mvpmatch.backendtask1.redis.service

import co.mvpmatch.backendtask1.config.ResourceNotFoundException
import co.mvpmatch.backendtask1.redis.repository.SessionRepository
import co.mvpmatch.backendtask1.vm.LoginLogout
import co.mvpmatch.backendtask1.vm.SessionEventPayload
import co.mvpmatch.backendtask1.web.api.model.SessionDTO
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant

@Service
@Profile("prod")
class SessionService(
    private val sessionRepository: SessionRepository,
    private var sessionEventPublisher: SessionEventPublisher
) {

    fun getSessions(userName: String): List<SessionDTO> {
        return sessionRepository.findAllByUsername(userName)
            .map {
                SessionDTO().apply {
                    sessionId = it.id
                    createdAt = it.createdAt.toOffsetDateTime()
                }
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

    fun publishLoginEvent(userName: String) {
        sessionEventPublisher.publish(
            SessionEventPayload(
                actionType = LoginLogout.LOGIN,
                username = userName,
                timestamp = Instant.now()
            )
        )
    }
}
