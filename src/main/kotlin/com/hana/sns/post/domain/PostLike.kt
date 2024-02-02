package com.hana.sns.post.domain

import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.post.infrastructure.postlike.PostLikeEntity
import com.hana.sns.user.domain.User
import com.hana.sns.user.infrastructure.UserEntity

data class PostLike (
    val user: User,
    val post: Post,
    var id: Long? = null
){

    constructor(postLikeEntity: PostLikeEntity): this (
        User(postLikeEntity.user),
        Post(postLikeEntity.post),
        postLikeEntity.id
    )

    fun toEntity(): PostLikeEntity {
        return PostLikeEntity(
            user = user.toEntity(),
            post = post.toEntity(),
            id = id
        )
    }


}