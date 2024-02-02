package com.hana.sns.user.controller.port

import com.hana.sns.user.controller.response.AlarmResponse
import com.hana.sns.user.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserService {
    fun join(userName: String, password: String): User
    fun login(userName: String, password: String): String
    fun loadUserByUserName(userName: String): User
    fun getAlarms(user: User, pageable: Pageable): Page<AlarmResponse>

}
