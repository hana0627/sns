package com.hana.sns.post.controller.response

import com.hana.sns.post.domain.Post
import com.hana.sns.user.controller.response.UserResponse
import java.time.LocalDateTime

data class PostResponse (
    var id: Int?,
    var title: String,
    var body: String,
    var user: UserResponse,
) {

    constructor(post: Post) : this (
        post.id,
        post.title,
        post.body,
        UserResponse(post.user),
    )
}