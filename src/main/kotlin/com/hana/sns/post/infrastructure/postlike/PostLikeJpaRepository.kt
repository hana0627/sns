package com.hana.sns.post.infrastructure.postlike

import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.user.infrastructure.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PostLikeJpaRepository : JpaRepository<PostLikeEntity, Int> {
    fun findByUserAndPost(user: UserEntity, post:PostEntity): PostLikeEntity?

    fun save(postLikeEntity: PostLikeEntity): PostLikeEntity

    fun deleteAllByPost(post: PostEntity)
}