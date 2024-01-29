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
        val postLikeEntity: PostLikeEntity? = postLikeJpaRepository.findByUserAndPost(UserEntity(user), PostEntity(post))
        return if(postLikeEntity != null) {
            PostLike(postLikeEntity)
        } else {
            null
        }

    }

    override fun save(postLike: PostLike): PostLike {
        return PostLike(postLikeJpaRepository.save(PostLikeEntity(postLike)))
    }

    override fun countByPost(post: Post): Long {
        return postLikeQueryRepository.countByPost(post)
    }

}
