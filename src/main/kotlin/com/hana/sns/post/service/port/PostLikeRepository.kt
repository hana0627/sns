package com.hana.sns.post.service.port

import com.hana.sns.post.domain.Post
import com.hana.sns.post.domain.PostLike
import com.hana.sns.user.domain.User

interface PostLikeRepository {

    fun findByUserAndPost(user: User, post: Post): PostLike?
    fun save(postLike: PostLike): PostLike
}
