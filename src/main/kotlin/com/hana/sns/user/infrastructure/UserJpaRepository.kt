package com.hana.sns.user.infrastructure

import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository : JpaRepository<UserEntity, Long> {

    fun findByUserName(userName: String): UserEntity?
}