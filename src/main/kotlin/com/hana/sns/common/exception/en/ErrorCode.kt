package com.hana.sns.common.exception.en

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String
) {
    // 예상하지 못한 서버에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    // 중복된 유저이름
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT,"User name is duplicated"),
    // 존재하지 않는 회원
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"User not founded"),
    // 존재하지 않는 게시글
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"Post not founded"),
    // 존재하지 않는 게시글
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED,"Permission is invalid"),
    // 패스워드 불일치
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Password is invalid"),
    // JWT 토큰 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"Token is invalid")

}
