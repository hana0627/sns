package com.hana.sns.user.infrastructure

import com.hana.sns.user.domain.User
import com.hana.sns.user.service.port.UserRepository
import lombok.RequiredArgsConstructor

@RequiredArgsConstructor
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
) : UserRepository {
    override fun findByUserName(userName: String): User? {
        val userEntity: UserEntity? = userJpaRepository.findByUserName(userName)
        return if(userEntity != null) {
            User(userEntity)
        } else {
            null
        }
    }

    override fun save(user: User): User {
       return User(userJpaRepository.save(UserEntity(user)))
    }
}