package com.hana.sns.user.domain

import com.hana.sns.user.domain.arg.AlarmArgs
import com.hana.sns.user.domain.en.AlarmType
import com.hana.sns.user.infrastructure.alarm.AlarmEntity
import java.time.LocalDateTime

data class Alarm(
    val user: User,
    val alarmType: AlarmType,
    val args: AlarmArgs,
    val registeredAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
    val id: Long? = null,
    ) {

    constructor(alarmEntity: AlarmEntity): this(
        User(alarmEntity.user),
        alarmEntity.alarmType,
        alarmEntity.alarmArgs,
        alarmEntity.registeredAt,
        alarmEntity.updatedAt,
        alarmEntity.deletedAt,
        alarmEntity.id
    )

}