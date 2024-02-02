package com.hana.sns.post.infrastructure.postlike

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.post.domain.Post
import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.post.infrastructure.postlike.QPostLikeEntity.postLikeEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component

@RequiredArgsConstructor
@Component
class PostLikeQueryRepository(
    private val queryFactory: JPAQueryFactory
) {

    fun countByPost(post: Post): Long {
        return queryFactory.select(postLikeEntity.count())
            .from(postLikeEntity)
            .where(postLikeEntity.post.eq(post.toEntity())).fetchOne() ?: throw SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,"Not found postLikeEntity")
    }

}