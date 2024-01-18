package com.hana.sns.user.repository

import com.hana.sns.user.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserEntityRepository : JpaRepository<UserEntity, Long> {

    fun findByUserName(userName: String): UserEntity?
}