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

    override fun modify(postId: Int, title: String, body: String, userName: String): Post {
        // 포스트존재여부
        val post: Post = postRepository.findById(postId)?: throw SnsApplicationException(ErrorCode.POST_NOT_FOUND,"post($postId) is not founded")
        // 포스트 작성자 == 수정하려는 사람
        if(post.user.userName != userName) {
            throw SnsApplicationException(ErrorCode.INVALID_PERMISSION,"$userName has no permission with post($postId)")
        }
        post.update(title, body)
        return postRepository.save(post)
    }

    override fun delete(postId: Int, userName: String?): Int {
// 포스트존재여부
        val post: Post = postRepository.findById(postId)?: throw SnsApplicationException(ErrorCode.POST_NOT_FOUND,"post($postId) is not founded")
        // 포스트 작성자 == 수정하려는 사람
        if(post.user.userName != userName) {
            throw SnsApplicationException(ErrorCode.INVALID_PERMISSION,"$userName has no permission with post($postId)")
        }
        //TODO delete 벌크연산 -> N+1문제 유의주시
        postRepository.delete(post)

        return postId
    }
}
