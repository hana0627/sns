package com.hana.sns.user.controller.response

import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.en.UserRole

data class UserResponse (
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
