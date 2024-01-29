package com.hana.sns.post.domain

import com.hana.sns.post.controller.request.CommentCreateRequest
import com.hana.sns.post.infrastructure.comment.CommentEntity
import com.hana.sns.user.domain.User
import java.time.LocalDateTime

data class Comment (
    val user: User,
    val post: Post,
    val comment: String,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    var id: Long? = null,
) {


    constructor(user: User, post: Post, createRequest: CommentCreateRequest) : this(
        user,
        post,
        createRequest.comment
    )

    constructor(commentEntity: CommentEntity) : this(
        User(commentEntity.user),
        Post(commentEntity.post),
        commentEntity.comment
    )


}