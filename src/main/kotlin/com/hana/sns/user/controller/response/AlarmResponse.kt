package com.hana.sns.user.controller.response

import com.hana.sns.user.domain.Alarm
import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.arg.AlarmArgs
import com.hana.sns.user.domain.en.AlarmType
import java.time.LocalDateTime

data class AlarmResponse(
    val alarmType: AlarmType,
    val args: AlarmArgs,
    val text: String, // 알람타입의 text
    val registeredAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
    val id: Long? = null,
) {

    constructor(alarm: Alarm): this(
        alarm.alarmType,
        alarm.args,
        alarm.alarmType.alarmText,
        alarm.registeredAt,
        alarm.updatedAt,
        alarm.deletedAt,
        alarm.id,
    )
}