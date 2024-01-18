package com.hana.sns.user.domain

import com.hana.sns.user.infrastructure.UserEntity
import com.hana.sns.user.domain.en.UserRole
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@NoArgsConstructor
data class User (
    val userName: String,
    val password: String,
    var userRole: UserRole? = null,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    val id: Int? = null,
    ){

    constructor(entity: UserEntity): this(
            entity.userName,
            entity.password,
            entity.role,
            entity.registeredAt,
            entity.updatedAt,
            entity.deletedAt,
            entity.id,
        )


    companion object {
        fun fixture(
            userName: String ="userName",
            password: String ="poassword",
            userRole: UserRole? = null,
            registeredAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null,
            id: Int? = null,

        ) : User {
            return User(
                userName = userName,
                password = password,
                userRole = userRole,
                registeredAt = registeredAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt,
                id = id,
            )
        }
    }
}
