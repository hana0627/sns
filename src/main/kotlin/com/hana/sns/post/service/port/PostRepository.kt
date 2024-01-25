package com.hana.sns.post.service.port

import com.hana.sns.post.domain.Post

interface PostRepository {

    fun save(post: Post): Post
    fun findById(postId: Int): Post?
    fun delete(post: Post)
}