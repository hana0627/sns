package com.hana.sns.user.model

import com.hana.sns.user.domain.UserEntity
import com.hana.sns.user.domain.en.UserRole
import lombok.NoArgsConstructor
import java.math.BigInteger
import java.sql.Timestamp
import java.time.LocalDateTime

@NoArgsConstructor
data class User (
    val id: Int?,
    val userName: String,
    val password: String,
    val userRole: UserRole?,
    var registeredAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
    var deletedAt: LocalDateTime?,

    ){

    constructor(entity: UserEntity): this(
            entity.id,
            entity.userName,
            entity.password,
            entity.role,
            entity.registeredAt,
            entity.updatedAt,
            entity.deletedAt
        )
}
