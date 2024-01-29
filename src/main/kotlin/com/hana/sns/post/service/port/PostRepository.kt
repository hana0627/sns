package com.hana.sns.post.service.port

import com.hana.sns.post.domain.Post
import com.hana.sns.user.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostRepository {

    fun save(post: Post): Post
    fun findById(postId: Long): Post?
    fun delete(post: Post)
    fun findAll(pageable: Pageable): Page<Post>
    fun findAllByUser(pageable: Pageable, user: User): Page<Post>
}
