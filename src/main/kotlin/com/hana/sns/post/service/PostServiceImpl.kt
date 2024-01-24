package com.hana.sns.post.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.post.controller.port.PostService
import com.hana.sns.post.domain.Post
import com.hana.sns.post.service.port.PostRepository
import com.hana.sns.user.domain.User
import com.hana.sns.user.service.port.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) : PostService {
    @Transactional
    override fun create(title: String, body: String, userName: String): Int{
        val user: User = userRepository.findByUserName(userName)?: throw SnsApplicationException(ErrorCode.USER_NOT_FOUND, "$userName is not founded")

        val post = Post(title, body, user)
        return postRepository.save(post).id!!
    }
}
