package com.hana.sns.post.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.post.controller.port.PostService
import com.hana.sns.post.controller.response.CommentResponse
import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Comment
import com.hana.sns.post.domain.Post
import com.hana.sns.post.domain.PostLike
import com.hana.sns.post.service.port.CommentRepository
import com.hana.sns.post.service.port.PostLikeRepository
import com.hana.sns.post.service.port.PostRepository
import com.hana.sns.user.domain.Alarm
import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.arg.AlarmArgs
import com.hana.sns.user.domain.en.AlarmType
import com.hana.sns.user.service.port.AlarmRepository
import com.hana.sns.user.service.port.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val postLikeRepository: PostLikeRepository,
    private val commentRepository: CommentRepository,
    private val alarmRepository: AlarmRepository,
) : PostService {
    @Transactional
    override fun create(title: String, body: String, user: User): Long{
        val post = Post(title, body, user)
        return postRepository.save(post).id!!
    }

    override fun modify(postId: Long, title: String, body: String, user: User): Post {
        // 포스트존재여부
        val post: Post = getPostbyIdOrException(postId)
        // 포스트 작성자 == 수정하려는 사람
        if(post.user != user) {
            throw SnsApplicationException(ErrorCode.INVALID_PERMISSION,"${user.userName} has no permission with post($postId)")
        }
        post.update(title, body)
        return postRepository.save(post)
    }

    @Transactional
    override fun delete(postId: Long, user: User): Long {
        // 포스트존재여부
        val post: Post = getPostbyIdOrException(postId)
        // 포스트 작성자 == 수정하려는 사람
        if(post.user != user) {
            throw SnsApplicationException(ErrorCode.INVALID_PERMISSION,"${user.userName} has no permission with post($postId)")
        }
        postLikeRepository.deleteAllByPost(post)
        // softDelete 방식이라 굳이 영속성객체에서
        // 데이터 정합성 문제가 발생하지 않을것 같음
        //em.flush()
        //em.clear()
        commentRepository.deleteAllByPost(post)
        //em.flush()
        //em.clear()
        postRepository.delete(post)

        return postId
    }

    override fun list(pageable: Pageable): Page<PostResponse> {
        return postRepository.findAll(pageable).map{PostResponse(it)}
    }

    override fun my(pageable: Pageable, user: User): Page<PostResponse> {
        return postRepository.findAllByUser(pageable, user).map{PostResponse(it)}
    }

    @Transactional
    override fun like(postId: Long, user: User): Long {
        val post: Post = getPostbyIdOrException(postId)
        val postLike : PostLike? = postLikeRepository.findByUserAndPost(user,post)
        if(postLike != null) {
            throw SnsApplicationException(ErrorCode.ALREADY_LIKED, "userName ${user.userName} already liked post $postId")
        }

        val alarm = Alarm(user,AlarmType.NEW_LIKE_ON_POST, AlarmArgs(user.id!!, post.user.id!!, post.id!!))
        alarmRepository.save(alarm)

        val result = postLikeRepository.save(PostLike(user, post))
        return result.id!!
    }


    override fun likeCount(postId: Long): Long {
        val post: Post = getPostbyIdOrException(postId)
        return postLikeRepository.countByPost(post)
    }

    override fun comment(postId: Long, user: User, comment: String): Long {
        val post: Post = getPostbyIdOrException(postId)

        val alarm = Alarm(user,AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(user.id!!, post.user.id!!, post.id!!))
        alarmRepository.save(alarm)

        return commentRepository.save(Comment(user, post, comment)).id!!
    }

    override fun getComments(postId: Long, pageable: Pageable): Page<CommentResponse> {
        val post:Post = postRepository.findById(postId) ?: throw SnsApplicationException(ErrorCode.POST_NOT_FOUND,"post($postId) is not founded")
        return commentRepository.findAllByPost(post, pageable).map { CommentResponse(it) };
    }




    private fun getPostbyIdOrException(postId: Long): Post {
        val post: Post = postRepository.findById(postId) ?: throw SnsApplicationException(
            ErrorCode.POST_NOT_FOUND,
            "post($postId) is not founded"
        )
        return post
    }

}
