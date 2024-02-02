package com.hana.sns.post.controller.port

import com.hana.sns.post.controller.request.CommentCreateRequest
import com.hana.sns.post.controller.response.CommentResponse
import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Post
import com.hana.sns.user.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostService {

    fun create(title: String, body: String, user: User): Long
    fun modify(postId: Long, title: String, body: String, user: User): Post
    fun delete(postId: Long, user: User): Long
    fun list(pageable: Pageable): Page<PostResponse>
    fun my(pageable: Pageable, user: User): Page<PostResponse>
    fun like(postId: Long, user: User): Long
    fun likeCount(postId: Long): Long
    fun comment(postId: Long, user: User, comment: String): Long
    fun getComments(postId: Long, pageable: Pageable): Page<CommentResponse>
}
