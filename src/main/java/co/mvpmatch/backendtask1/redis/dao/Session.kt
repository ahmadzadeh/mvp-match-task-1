package co.mvpmatch.backendtask1.redis.dao

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.Id

@RedisHash("sessions")
data class Session(
    @Id
    private val id: String,

    @Indexed
    private val username: String,

    private val createdAt: ZonedDateTime
) : Serializable
