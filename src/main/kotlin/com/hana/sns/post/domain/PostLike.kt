package com.hana.sns.post.domain

import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.post.infrastructure.postlike.PostLikeEntity
import com.hana.sns.user.domain.User
import com.hana.sns.user.infrastructure.UserEntity
import java.time.LocalDateTime

data class PostLike (
    val user: User,
    val post: Post,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    var id: Long? = null
){

    constructor(postLikeEntity: PostLikeEntity): this (
        User(postLikeEntity.user),
        Post(postLikeEntity.post),
        postLikeEntity.registeredAt,
        postLikeEntity.updatedAt,
        postLikeEntity.deletedAt,
        postLikeEntity.id
    )

    fun toEntity(): PostLikeEntity {
        return PostLikeEntity(
            user = user.toEntity(),
            post = post.toEntity(),
            registeredAt = registeredAt,
            updatedAt = updatedAt,
            deletedAt = deletedAt,
            id = id
        )
    }


}