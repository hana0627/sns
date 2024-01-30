package com.hana.sns.user.domain

import com.hana.sns.user.domain.arg.AlarmArgs
import com.hana.sns.user.domain.en.AlarmType
import com.hana.sns.user.infrastructure.alarm.AlarmEntity
import java.time.LocalDateTime

data class Alarm(
    val user: User,
    val alarmType: AlarmType,
    val args: AlarmArgs,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    var id: Long? = null,
) {

    constructor(alarmEntity: AlarmEntity) : this(
        User(alarmEntity.user),
        alarmEntity.alarmType,
        alarmEntity.alarmArgs,
        alarmEntity.registeredAt,
        alarmEntity.updatedAt,
        alarmEntity.deletedAt,
        alarmEntity.id
    )


    companion object {
        fun fixture(
            user: User = User("userName","password"),
            alarmType: AlarmType = AlarmType.NEW_COMMENT_ON_POST,
            args: AlarmArgs = AlarmArgs(1L, 2L, 1L),
            registeredAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null,
            id: Long? = null,
        ) : Alarm {
            return Alarm(
                user,
                alarmType,
                args,
                registeredAt,
                updatedAt,
                deletedAt,
                id
            )

        }

    }
}