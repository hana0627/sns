package com.hana.sns.user.service.port

import com.hana.sns.user.domain.User

interface UserRepository {
    fun findByUserName(userName: String): User?
    fun save(user: User): User
}