package com.hana.sns.common.config

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.hana.sns.user.domain.User
import io.lettuce.core.RedisURI
import lombok.RequiredArgsConstructor
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
class RedisConfig(
    private val redisProperties: RedisProperties
) {

    // 커넥션 정보 설정
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisURI = RedisURI.Builder.redis(redisProperties.host, redisProperties.port).build()
        val configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI)
        val factory = LettuceConnectionFactory(configuration)
        factory.afterPropertiesSet()
        return factory
    }

    // RedisTemplate 설정
    @Bean
    fun userRedisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, User> {
        val redisTemplate: RedisTemplate<String, User> = RedisTemplate()
        redisTemplate.connectionFactory = redisConnectionFactory
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(User::class.java)
        return redisTemplate

    }

}
