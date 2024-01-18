package com.hana.sns.user.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.user.controller.request.UserJoinRequest
import com.hana.sns.user.controller.request.UserLoginRequest
import com.hana.sns.user.controller.response.UserJoinResponse
import com.hana.sns.user.controller.response.UserLoginResponse
import com.hana.sns.user.domain.User
import com.hana.sns.user.service.UserServiceImpl
import lombok.Builder
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RequiredArgsConstructor
@RestController
class UserController (
    private val userService: UserServiceImpl
) {

    @PostMapping("/api/v1/users/join")
    fun join(@RequestBody request: UserJoinRequest): Response<UserJoinResponse> {
        val user: User = userService.join(request.userName, request.password)
        return Response.success(UserJoinResponse(user))
    }
    @PostMapping("/api/v1/users/login")
    fun login(@RequestBody request: UserLoginRequest): Response<UserLoginResponse> {
        val token: String = userService.login(request.userName, request.password)
        return Response.success(UserLoginResponse(token))
    }
}