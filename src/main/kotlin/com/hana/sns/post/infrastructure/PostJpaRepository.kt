package com.hana.sns.post.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface PostJpaRepository : JpaRepository<PostEntity, Long>{
}