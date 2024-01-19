package com.hana.sns.post.service

import com.hana.sns.post.controller.port.PostService
import com.hana.sns.post.service.port.PostRepository
import com.hana.sns.user.service.port.UserRepository
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) : PostService {
    override fun create(title: String, body: String, userName: String) {
        // user find
//        val user: User = userRepository.findByUserName(userName)?: throw SnsApplicationException(ErrorCode.USER_NOT_FOUND, "$userName is not founded")
        // post save


        // return
    }
}