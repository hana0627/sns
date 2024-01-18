package com.hana.sns.common.exception

import com.hana.sns.common.exception.en.ErrorCode

data class SnsApplicationException(
    val errorCode: ErrorCode,
    override val message: String?

) : RuntimeException() {

    val getMessage: String
        get() = if(message == null) {
             errorCode.message
        } else {
            String.format("${errorCode.message}, $message")
        }
}