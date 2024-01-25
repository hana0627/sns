package com.hana.sns.post.service.port

import com.hana.sns.post.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostRepository {

    fun save(post: Post): Post
    fun findById(postId: Int): Post?
    fun delete(post: Post)
    fun findAll(pageable: Pageable) : Page<Post>
}