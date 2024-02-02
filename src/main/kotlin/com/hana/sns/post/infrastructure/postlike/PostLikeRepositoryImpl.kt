package com.hana.sns.post.infrastructure.postlike

import com.hana.sns.post.domain.Post
import com.hana.sns.post.domain.PostLike
import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.post.service.port.PostLikeRepository
import com.hana.sns.user.domain.User
import com.hana.sns.user.infrastructure.UserEntity
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class PostLikeRepositoryImpl (
    private val postLikeJpaRepository: PostLikeJpaRepository,
    private val postLikeQueryRepository: PostLikeQueryRepository
) : PostLikeRepository {
    override fun findByUserAndPost(user: User, post: Post): PostLike? {
        println("오이")
        val postLikeEntity: PostLikeEntity? = postLikeJpaRepository.findByUserAndPost(user.toEntity(), post.toEntity())
        println(postLikeEntity)
        return if(postLikeEntity != null) {
            PostLike(postLikeEntity)
        } else {
            null
        }

    }

    override fun save(postLike: PostLike): PostLike {
        return PostLike(postLikeJpaRepository.save(postLike.toEntity()))
    }

    override fun countByPost(post: Post): Long {
        return postLikeQueryRepository.countByPost(post)
    }

    override fun deleteAllByPost(post: Post) {
        return postLikeJpaRepository.deleteAllByPost(post.toEntity())
    }

}
