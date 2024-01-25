package com.hana.sns.user.controller.port

import com.hana.sns.user.domain.User

interface UserService {
    fun join(userName: String, password: String): User
    fun login(userName: String, password: String): String
    fun loadUserByUserName(userName: String): User

}
