package com.hana.sns.post.infrastructure

import com.hana.sns.post.service.port.PostRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class PostRepositoryImpl (
    private val postJpaRepository: PostJpaRepository,
) : PostRepository {
}