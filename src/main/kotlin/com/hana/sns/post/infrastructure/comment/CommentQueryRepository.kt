package com.hana.sns.post.infrastructure.comment

import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.post.infrastructure.comment.QCommentEntity.commentEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import java.time.LocalDateTime


@Component
@RequiredArgsConstructor
class CommentQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun deleteAllByPost(post: PostEntity) {
        queryFactory.update(commentEntity)
            .set(commentEntity.deletedAt, LocalDateTime.now())
            .where(commentEntity.post.eq(post))
    }


}