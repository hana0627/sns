package com.hana.sns.user.controller.request

data class UserLoginRequest (
    private val userName: String,
    private val password: String,
) {
}