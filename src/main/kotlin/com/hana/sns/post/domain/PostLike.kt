package com.hana.sns.post.domain

import com.hana.sns.post.infrastructure.postlike.PostLikeEntity
import com.hana.sns.user.domain.User

data class PostLike (
    val user: User,
    val post: Post,
    var id: Int? = null
){

    constructor(postLikeEntity: PostLikeEntity): this (
        User(postLikeEntity.user),
        Post(postLikeEntity.post),
        postLikeEntity.id
    )
//    constructor(postEntity: PostEntity, userEntity: UserEntity): this(
//        User(userEntity),
//        Post(postEntity),
//    )


}