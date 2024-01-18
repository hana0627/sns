package com.hana.sns.user.controller.response

import com.hana.sns.user.domain.UserEntity
import com.hana.sns.user.domain.en.UserRole
import com.hana.sns.user.model.User

data class UserJoinResponse (
    val id: Int?,
    val userName: String,
    val userRole : UserRole?,
    ){

    constructor(user: User): this(
        user.id,
        user.userName,
        user.userRole
    )
}