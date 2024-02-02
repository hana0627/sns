package com.hana.sns.user.service.port

import com.hana.sns.user.domain.Alarm
import com.hana.sns.user.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AlarmRepository {
//    fun findAllByUser(user: User, pageable: Pageable): Page<Alarm>
    fun findAllByUserId(userId: Long, pageable: Pageable): Page<Alarm>
    fun save(alarm:Alarm): Alarm
}