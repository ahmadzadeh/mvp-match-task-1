package co.mvpmatch.backendtask1.vm

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.Instant

data class SessionEventPayload(
    @JsonProperty val username: String,
    @JsonProperty val loginAt: Instant,
) : Serializable {
    @JsonCreator
    constructor() : this("", Instant.EPOCH)
}

