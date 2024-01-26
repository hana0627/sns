package com.hana.sns.post.infrastructure

import com.hana.sns.user.infrastructure.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostJpaRepository : JpaRepository<PostEntity, Long>{
    fun findAllByUser(pageable: Pageable, user: UserEntity) : Page<PostEntity>
}
