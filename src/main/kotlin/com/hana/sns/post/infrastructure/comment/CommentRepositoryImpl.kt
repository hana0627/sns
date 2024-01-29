package com.hana.sns.post.infrastructure.comment

import com.hana.sns.post.domain.Comment
import com.hana.sns.post.service.port.CommentRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class CommentRepositoryImpl(
    private val commentJpaRepository: CommentJpaRepository,
) : CommentRepository{
    override fun save(comment: Comment): Comment {
        return Comment(commentJpaRepository.save(CommentEntity(comment)))
    }
}
