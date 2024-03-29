package com.hana.sns.post.infrastructure.postlike

import com.hana.sns.post.domain.PostLike
import com.hana.sns.post.infrastructure.PostEntity
import com.hana.sns.user.infrastructure.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "postLike")
//@SQLDelete(sql = "UPDATE post_like SET deleted_at = NOW(6) WHERE id=?")
//@SQLRestriction("deleted_at is NULL")
class PostLikeEntity (

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    val user: UserEntity,
    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    val post: PostEntity,
    @Column(name = "registered_at")
    var registeredAt: LocalDateTime? = null,
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,
    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    ){


    @PrePersist
    fun registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

    @PreUpdate
    fun updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

//    constructor(postLike: PostLike): this(
//        UserEntity(postLike.user),
//        PostEntity(postLike.post),
//    )


}
