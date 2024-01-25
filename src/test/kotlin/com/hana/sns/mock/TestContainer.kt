package com.hana.sns.mock

import com.hana.sns.post.controller.PostController
import com.hana.sns.post.service.PostServiceImpl
import com.hana.sns.user.controller.UserController
import com.hana.sns.user.service.UserServiceImpl

class TestContainer(
    val passwordEncoder: FakePasswordEncoder,

    val userRepository: FakeUserRepository,
    val postRepository: FakePostRepository,


    val userService: UserServiceImpl,
    val postService: PostServiceImpl,


    val userController: UserController,
    val postController: PostController,

    ) {

    companion object {
        fun build(): TestContainer {
            val passwordEncoder = FakePasswordEncoder()


            val userRepository = FakeUserRepository()
            val postRepository = FakePostRepository()

            val userService = UserServiceImpl(userRepository, passwordEncoder)
            val postService = PostServiceImpl(postRepository, userRepository)

            val userController = UserController(userService)
            val postController = PostController(postService)

            return TestContainer(
                passwordEncoder,
                userRepository,
                postRepository,
                userService,
                postService,
                userController,
                postController,
            )
        }
    }
}
