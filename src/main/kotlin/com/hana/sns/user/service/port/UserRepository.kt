package com.hana.sns.user.service.port

import com.hana.sns.user.domain.User
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun findByUserName(userName: String): User?
    fun save(user: User): User
}