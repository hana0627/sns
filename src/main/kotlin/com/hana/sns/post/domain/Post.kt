package com.hana.sns.post.domain

import com.hana.sns.user.infrastructure.UserEntity
import jakarta.persistence.*
import java.time.LocalDateTime

data class Post(
    val title: String,
    val body: String,
    val user: UserEntity,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    val id: Int? = null,
) {
}