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
    private val commentQueryRepository: CommentQueryRepository
) : CommentRepository{
    override fun save(comment: Comment): Comment {
        return Comment(commentJpaRepository.save(comment.toEntity()))
    }

    override fun findAllByPost(post: Post, pageable: Pageable): Page<Comment> {
        return commentJpaRepository.findAllByPost(pageable, post.toEntity()).map { Comment(it) }
    }

    override fun deleteAllByPost(post: Post) {
        return commentQueryRepository.deleteAllByPost(post.toEntity())
    }
}
