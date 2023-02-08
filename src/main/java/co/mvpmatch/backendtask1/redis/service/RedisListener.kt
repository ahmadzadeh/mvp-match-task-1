package co.mvpmatch.backendtask1.redis.service

import co.mvpmatch.backendtask1.vm.SessionEventPayload
import org.springframework.stereotype.Service


@Service
class RedisListener {
    fun handleMessage(payload: SessionEventPayload) {
        println("New signIn: ${payload.username}")
    }
}
