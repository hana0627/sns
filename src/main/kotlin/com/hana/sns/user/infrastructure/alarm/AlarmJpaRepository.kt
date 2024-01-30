package com.hana.sns.user.infrastructure.alarm

import com.hana.sns.user.infrastructure.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AlarmJpaRepository : JpaRepository<AlarmEntity, Long> {
    fun findAllByUser(user: UserEntity, pageable: Pageable) : Page<AlarmEntity>
    fun save(alarmEntity: AlarmEntity): AlarmEntity
}