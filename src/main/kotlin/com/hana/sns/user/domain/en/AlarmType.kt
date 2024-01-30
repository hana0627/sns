package com.hana.sns.user.domain.en
enum class AlarmType (
    val alarmText: String
){
    NEW_COMMENT_ON_POST("new comment!"),
    NEW_LIKE_ON_POST("new like!")
}