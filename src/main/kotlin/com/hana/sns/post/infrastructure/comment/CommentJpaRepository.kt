package com.hana.sns.post.infrastructure.comment

import com.hana.sns.post.infrastructure.PostEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CommentJpaRepository : JpaRepository<CommentEntity, Long>{
    fun findAllByPost(pageable: Pageable, postEntity: PostEntity): Page<CommentEntity>
}