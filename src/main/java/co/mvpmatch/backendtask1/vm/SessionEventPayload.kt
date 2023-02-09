package co.mvpmatch.backendtask1.vm

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.Instant

data class SessionEventPayload(
    @JsonProperty val actionType: LoginLogout,
    @JsonProperty val username: String,
    @JsonProperty val timestamp: Instant,
) : Serializable {
    @JsonCreator
    constructor() : this(LoginLogout.UNDEFINED, "", Instant.EPOCH)
}

enum class LoginLogout {
    LOGIN, LOGOUT, UNDEFINED
}
