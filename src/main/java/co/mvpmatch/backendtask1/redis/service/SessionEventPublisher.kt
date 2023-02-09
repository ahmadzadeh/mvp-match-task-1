package co.mvpmatch.backendtask1.redis.service

import co.mvpmatch.backendtask1.vm.SessionEventPayload
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service

@Service
@Profile("prod")
class SessionEventPublisher(
    private val redisTemplate: RedisTemplate<String, SessionEventPayload>,
    private val topic: ChannelTopic,
) {
    fun publish(payload: SessionEventPayload) {
        redisTemplate.convertAndSend(topic.topic, payload)
    }
}
