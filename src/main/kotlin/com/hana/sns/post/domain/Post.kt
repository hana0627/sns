package com.hana.sns.post.domain

import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.user.domain.User
import java.time.LocalDateTime

data class Post(
    var title: String,
    var body: String,
    val user: User,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    var id: Int? = null,
) {

    constructor(postEntity: PostEntity) : this(
        postEntity.title,
        postEntity.body,
        User(postEntity.user),
        postEntity.registeredAt,
        postEntity.updatedAt,
        postEntity.deletedAt,
        postEntity.id,
    )

    companion object {
        fun fixture(
            title: String = "title",
            body: String = "body",
            user: User = User.fixture("userName", "password", null, null, null, null, 1),
        ): Post {
            return Post(
                title = title,
                body = body,
                user = user,
            )
        }
    }

    fun update(title:String, body: String) {
        this.title = title
        this.body = body
    }


}