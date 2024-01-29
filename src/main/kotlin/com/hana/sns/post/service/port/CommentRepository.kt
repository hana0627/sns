package com.hana.sns.post.service.port

import com.hana.sns.post.domain.Comment

interface CommentRepository {
    fun save(comment: Comment): Comment
}