package com.hana.sns.common.exception.en

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User name is duplicated"),
}