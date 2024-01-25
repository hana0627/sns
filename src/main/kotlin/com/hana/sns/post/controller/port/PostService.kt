package com.hana.sns.post.controller.port

import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostService {

    fun create(title: String, body: String, userName: String): Int
    fun modify(postId: Int, title: String, body: String, userName: String): Post
    fun delete(postId: Int, userName: String?): Int
    fun list(pageable: Pageable): Page<PostResponse>
}