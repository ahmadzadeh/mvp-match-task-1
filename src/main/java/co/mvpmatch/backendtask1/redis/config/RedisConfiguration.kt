package co.mvpmatch.backendtask1.redis.config

import co.mvpmatch.backendtask1.config.ApplicationProperties
import co.mvpmatch.backendtask1.redis.service.SessionEventListener
import co.mvpmatch.backendtask1.vm.SessionEventPayload
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import redis.clients.jedis.JedisPoolConfig


@Configuration
@Profile("prod")
class RedisConfiguration(
    private val sessionEventListener: SessionEventListener
) {
    @Value("\${spring.redis.host}")
    private lateinit var redisHost: String

    @Value("\${spring.redis.port}")
    private lateinit var redisPort: String

    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory(
            RedisStandaloneConfiguration(redisHost, redisPort.toInt())
        )
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.setConnectionFactory(jedisConnectionFactory())
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericToStringSerializer(Any::class.java)
        return template
    }

    @Bean("SessionEventRedisTemplate")
    fun sessionEventRedisTemplate(): RedisTemplate<String, SessionEventPayload> {
        val template = RedisTemplate<String, SessionEventPayload>()
        template.setConnectionFactory(jedisConnectionFactory())
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer(getMapper())
        return template
    }

    @Bean
    fun messageListener(): MessageListenerAdapter {
        val listener = MessageListenerAdapter(sessionEventListener)
        val serializer = Jackson2JsonRedisSerializer(SessionEventPayload::class.java)
        serializer.setObjectMapper(getMapper())
        listener.setSerializer(serializer)
        return listener
    }

    @Bean
    fun redisContainer(): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(jedisConnectionFactory())
        container.addMessageListener(messageListener(), topic())
        return container
    }

    @Bean
    fun topic(): ChannelTopic {
        return ChannelTopic("session")
    }

    private fun getMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        return mapper

    }
}
