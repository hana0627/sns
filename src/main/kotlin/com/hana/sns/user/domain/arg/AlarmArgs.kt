package com.hana.sns.user.domain.arg

data class AlarmArgs(
    // 알림을 발생시킨 사람
    val fromUserId: Long,
    // 알림을 전달받을 사람
    val targetId: Long,
) {
}