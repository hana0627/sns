package com.hana.sns.user.infrastructure

import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.en.UserRole
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "user_account")
@SQLDelete(sql = "UPDATE user_account SET deleted_at = NOW(6) WHERE id=?")
@SQLRestriction("deleted_at is NULL")
class UserEntity (

    @Column(name = "user_name")
    val userName: String,

    @Column(name = "password")
    val password: String,
    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    val role: UserRole? = null,

    @Column(name = "registered_at")
    var registeredAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
){

    constructor(user: User): this(
        user.userName,
        user.password,
        user.userRole
    )

    @PrePersist
    fun registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

    @PreUpdate
    fun updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

}