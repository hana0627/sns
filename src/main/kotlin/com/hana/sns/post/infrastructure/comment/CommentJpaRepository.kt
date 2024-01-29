package com.hana.sns.post.infrastructure.comment

import org.springframework.data.jpa.repository.JpaRepository

interface CommentJpaRepository : JpaRepository<CommentEntity, Long>{
}