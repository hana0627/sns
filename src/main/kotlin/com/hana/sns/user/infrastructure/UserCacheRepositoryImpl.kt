package com.hana.sns.user.infrastructure

import com.hana.sns.common.config.USER_CACHE_TTL
import com.hana.sns.user.domain.User
import com.hana.sns.user.service.port.UserCacheRepository
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class UserCacheRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, User>

) : UserCacheRepository {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun setUser(user: User) {
        val key = "USER:${user.userName}"
        log.info("Set User to Redis {}, {}", key, user);
        redisTemplate.opsForValue().set(key, user, USER_CACHE_TTL)
    }
    override fun getUser(userName: String): User? {
        val key = "USER:$userName"
        val user = redisTemplate.opsForValue().get(key)
        log.info("Get data from Redis {} , {}", key, user);
        return user
    }
}
