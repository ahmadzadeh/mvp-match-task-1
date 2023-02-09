package co.mvpmatch.backendtask1.redis.config


import co.mvpmatch.backendtask1.config.ApplicationProperties
import co.mvpmatch.backendtask1.config.CACHE_USER_SALT
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import java.time.Duration

@Configuration
@EnableCaching
@Profile("prod")
class CacheConfiguration(
    private val prop: ApplicationProperties
) {
    @Bean
    fun redisCacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer? {
        return RedisCacheManagerBuilderCustomizer { builder: RedisCacheManager.RedisCacheManagerBuilder ->
            builder
                .withCacheConfiguration(
                    CACHE_USER_SALT,
                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(prop.cacheLifeTimeSec))
                )
        }
    }
}
