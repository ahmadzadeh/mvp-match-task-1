package co.mvpmatch.backendtask1.redis.repository

import co.mvpmatch.backendtask1.redis.domain.Session
import org.springframework.data.repository.CrudRepository

interface SessionRepository : CrudRepository<Session, String> {
    fun findAllByUsername(username: String): List<Session>
}
