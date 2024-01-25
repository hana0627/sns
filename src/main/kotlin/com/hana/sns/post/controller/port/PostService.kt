package com.hana.sns.post.controller.port

import com.hana.sns.post.domain.Post

interface PostService {

    fun create(title: String, body: String, userName: String): Int
    fun modify(postId: Int, title: String, body: String, userName: String): Post
}