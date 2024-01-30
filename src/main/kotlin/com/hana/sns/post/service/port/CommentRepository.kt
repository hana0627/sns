package com.hana.sns.post.service.port

import com.hana.sns.post.domain.Comment
import com.hana.sns.post.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CommentRepository {
    fun save(comment: Comment): Comment
    fun findAllByPost(post: Post, pageable: Pageable): Page<Comment>
    fun deleteAllByPost(post: Post)
}