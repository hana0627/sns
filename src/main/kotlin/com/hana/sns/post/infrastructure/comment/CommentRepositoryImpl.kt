package com.hana.sns.post.infrastructure.comment

import com.hana.sns.post.domain.Comment
import com.hana.sns.post.domain.Post
import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.post.service.port.CommentRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class CommentRepositoryImpl(
    private val commentJpaRepository: CommentJpaRepository,
) : CommentRepository{
    override fun save(comment: Comment): Comment {
        return Comment(commentJpaRepository.save(CommentEntity(comment)))
    }

    override fun findAllByPost(post: Post, pageable: Pageable): Page<Comment> {
        return commentJpaRepository.findAllByPost(pageable, PostEntity(post)).map { Comment(it) }
    }
}
