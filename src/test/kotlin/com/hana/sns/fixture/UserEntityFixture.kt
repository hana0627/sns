package com.hana.sns.fixture

import com.hana.sns.user.domain.UserEntity

class UserEntityFixture {

    companion object {
        fun get(userName: String, password: String): UserEntity {
            return UserEntity(userName,password)
        }
    }
}