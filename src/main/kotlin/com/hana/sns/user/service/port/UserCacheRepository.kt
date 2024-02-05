package com.hana.sns.user.service.port

import com.hana.sns.user.domain.User

interface UserCacheRepository {
    fun setUser(user: User)
    fun getUser(userName: String): User?
}
