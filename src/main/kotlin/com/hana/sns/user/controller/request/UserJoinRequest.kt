package com.hana.sns.user.controller.request

data class UserJoinRequest (
    private val userName: String,
    private val password: String,
) {
}