package co.mvpmatch.backendtask1.redis.domain

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.Id

@RedisHash("sessions")
data class Session(
    @Id
    var id: String,

    @Indexed
    var username: String,

    var createdAt: ZonedDateTime
) : Serializable
