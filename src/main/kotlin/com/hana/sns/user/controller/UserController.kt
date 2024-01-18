package com.hana.sns.user.controller

import com.hana.sns.user.model.User
import com.hana.sns.user.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RequiredArgsConstructor
@RestController
class UserController (
    private val userService: UserService
) {

    // TODO: implement
    @PostMapping("/api/v1/users/join")
    fun join() {
        userService.join("","")
    }
    // TODO: implement
    @PostMapping("/api/v1/users/login")
    fun login() {
        userService.login()
    }
}