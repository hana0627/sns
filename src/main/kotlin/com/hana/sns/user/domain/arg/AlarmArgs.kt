package com.hana.sns.user.domain.arg

data class AlarmArgs(
    // 알림을 발생시킨 사람
    val fromUserId: Long,
    // 알림을 전달받을 사람
    val targetUserId: Long,
    // 알람이 온 포스트
    val targetPostId: Long,
) {
}