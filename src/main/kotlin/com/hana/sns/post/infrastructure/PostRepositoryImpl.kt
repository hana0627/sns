package com.hana.sns.post.infrastructure

import com.hana.sns.post.domain.Post
import com.hana.sns.post.service.port.PostRepository
import com.hana.sns.user.domain.User
import com.hana.sns.user.infrastructure.UserEntity
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class PostRepositoryImpl (
    private val postJpaRepository: PostJpaRepository,
) : PostRepository {
    override fun save(post: Post): Post {
        return Post(postJpaRepository.save(PostEntity(post)))
    }

    override fun findById(postId: Long): Post? {
        val postEntity: PostEntity? = postJpaRepository.findById(postId).orElse(null)
        return if (postEntity != null) {
            Post(postEntity)
        } else {
            null
        }
    }

    override fun delete(post: Post) {
        return postJpaRepository.delete(PostEntity(post))
    }

    override fun findAll(pageable: Pageable): Page<Post> {
        return postJpaRepository.findAll(pageable).map{Post(it)}
    }

    override fun findAllByUser(pageable: Pageable, user: User): Page<Post> {
        return postJpaRepository.findAllByUser(pageable, UserEntity(user)).map { Post(it) }
    }
}
