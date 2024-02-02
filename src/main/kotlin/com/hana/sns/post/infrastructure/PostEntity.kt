package com.hana.sns.post.infrastructure

import com.hana.sns.post.domain.Post
import com.hana.sns.user.domain.User
import com.hana.sns.user.infrastructure.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted_at = NOW(6) WHERE id=?")
@SQLRestriction("deleted_at is NULL")
class PostEntity(
    @Column(name = "title")
    val title: String,

    @Column(name = "body", columnDefinition = "TEXT")
    val body: String,

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    val user: UserEntity,

    @Column(name = "registered_at")
    var registeredAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) {

    @PrePersist
    fun registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

    @PreUpdate
    fun updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

}