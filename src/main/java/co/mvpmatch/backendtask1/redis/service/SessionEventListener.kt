package co.mvpmatch.backendtask1.redis.service

import co.mvpmatch.backendtask1.config.CACHE_USER_SALT
import co.mvpmatch.backendtask1.redis.domain.Session
import co.mvpmatch.backendtask1.redis.repository.SessionRepository
import co.mvpmatch.backendtask1.service.UserService
import co.mvpmatch.backendtask1.vm.LoginLogout
import co.mvpmatch.backendtask1.vm.SessionEventPayload
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*


@Service
class SessionEventListener(
    // beans of this class must be loaded @Lazy because other beans in redisConfig should be initialized first.
    @Lazy private val sessionRepository: SessionRepository,
    @Lazy private val cacheManager: CacheManager,
    @Lazy private val userService: UserService,
) {
    fun handleMessage(payload: SessionEventPayload) {
        if (payload.actionType == LoginLogout.LOGIN) {
            sessionRepository.save(
                Session(
                    UUID.randomUUID().toString(),
                    payload.username,
                    ZonedDateTime.now()
                )
            )
        } else {
            cacheManager.getCache(CACHE_USER_SALT)?.evict(payload.username)
            sessionRepository.deleteAllById(sessionRepository.findAllByUsername(payload.username).map { it.id })
            val user = userService.getByUserName(payload.username)
            user.jwtSalt = UUID.randomUUID().toString() // this will invalidate current tokens
            userService.saveUser(user)
        }
    }
}
