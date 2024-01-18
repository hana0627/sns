package com.hana.sns.user.controller.request

data class UserLoginRequest (
    val userName: String,
    val password: String,
) {
}