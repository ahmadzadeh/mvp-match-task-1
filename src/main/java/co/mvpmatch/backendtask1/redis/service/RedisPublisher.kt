package co.mvpmatch.backendtask1.redis.service

import co.mvpmatch.backendtask1.vm.SessionEventPayload
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service


@Service
class RedisPublisher(
    private val redisTemplate: RedisTemplate<SessionEventPayload, Any>,
    private val topic: ChannelTopic,
) {
    fun publish(payload: SessionEventPayload) {
        redisTemplate.convertAndSend(topic.topic, payload)
    }
}
