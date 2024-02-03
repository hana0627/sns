package com.hana.sns.post.infrastructure.postlike

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.post.infrastructure.postlike.QPostLikeEntity.postLikeEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@RequiredArgsConstructor
@Component
class PostLikeQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun countByPost(post: PostEntity): Long {
        return queryFactory.select(postLikeEntity.count())
            .from(postLikeEntity)
            .where(postLikeEntity.post.eq(post)).fetchOne() ?: throw SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,"Not found postLikeEntity")
    }

    fun deleteAllByPost(post: PostEntity) {
        queryFactory.update(postLikeEntity)
            .set(postLikeEntity.deletedAt, LocalDateTime.now())
            .where(postLikeEntity.post.eq(post)).execute()
    }

}