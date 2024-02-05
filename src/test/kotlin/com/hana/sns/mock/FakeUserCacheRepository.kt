package com.hana.sns.mock

import com.hana.sns.user.domain.User
import com.hana.sns.user.service.port.UserCacheRepository

class FakeUserCacheRepository : UserCacheRepository {
    val map = HashMap<String, User> ()

    override fun setUser(user: User) {
        val key = "USER:${user.userName}"
        map.put(key,user)
    }

    override fun getUser(userName: String): User? {
        val key = "USER:$userName"
        val user: User? = map.get(key)
        return user
    }
}