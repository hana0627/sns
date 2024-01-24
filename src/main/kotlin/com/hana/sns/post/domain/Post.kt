package com.hana.sns.post.domain

import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.user.domain.User
import java.time.LocalDateTime

data class Post(
    val title: String,
    val body: String,
    val user: User,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    var id: Int? = null,
) {

    constructor(postEntity: PostEntity) : this(
        postEntity.title,
        postEntity.body,
        User(postEntity.user),
        postEntity.registeredAt,
        postEntity.updatedAt,
        postEntity.deletedAt,
        postEntity.id,
    )
}