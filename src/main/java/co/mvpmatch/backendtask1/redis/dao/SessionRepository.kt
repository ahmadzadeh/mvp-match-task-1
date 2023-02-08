package co.mvpmatch.backendtask1.redis.dao

import co.mvpmatch.backendtask1.redis.dao.Session
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SessionRepository : CrudRepository<Session, UUID> {
    fun findAllByUserId(userId: Long): List<Session>
    fun deleteAllByUserId(userId: Long)
}
