package com.hana.sns.mock

import com.hana.sns.user.controller.UserController
import com.hana.sns.user.service.UserServiceImpl

class TestContainer(
    val passwordEncoder: FakePasswordEncoder,


    val userRepository: FakeUserRepository,


    val userService: UserServiceImpl,


    val userController: UserController,

    ) {

    companion object {
        fun build(): TestContainer {
            val userRepository = FakeUserRepository()
            val passwordEncoder = FakePasswordEncoder()
            val userService = UserServiceImpl(userRepository, passwordEncoder)

            val userController = UserController(userService)

            return TestContainer(
                passwordEncoder,
                userRepository,
                userService,
                userController,
            )
        }
    }

}