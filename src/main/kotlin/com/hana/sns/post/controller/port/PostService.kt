package com.hana.sns.post.controller.port

import com.hana.sns.post.controller.request.CommentCreateRequest
import com.hana.sns.post.controller.response.CommentResponse
import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostService {

    fun create(title: String, body: String, userName: String): Long
    fun modify(postId: Long, title: String, body: String, userName: String): Post
    fun delete(postId: Long, userName: String?): Long
    fun list(pageable: Pageable): Page<PostResponse>
    fun my(pageable: Pageable, userName: String): Page<PostResponse>
    fun like(postId: Long, userName: String?): Long
    fun likeCount(postId: Long): Long
    fun comment(postId: Long, userName: String?, comment: String): Long
    fun getComments(postId: Long, pageable: Pageable): Page<CommentResponse>
}
