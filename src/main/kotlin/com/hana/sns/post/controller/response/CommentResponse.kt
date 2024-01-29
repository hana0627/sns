package com.hana.sns.post.controller.response

import com.hana.sns.post.domain.Comment
import com.hana.sns.post.domain.Post
import com.hana.sns.user.domain.User
import java.time.LocalDateTime

data class CommentResponse(
    val comment: String,
    val user: User,
    val post: Post,
    val registeredAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
    val id: Long?,
    ) {

    constructor(comment: Comment) :this(
        comment.comment,
        comment.user,
        comment.post,
        comment.registeredAt,
        comment.updatedAt,
        comment.deletedAt,
        comment.id,
    )
}