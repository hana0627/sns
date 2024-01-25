package com.hana.sns.post.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.FakePasswordEncoder
import com.hana.sns.mock.FakePostRepository
import com.hana.sns.mock.FakeUserRepository
import com.hana.sns.mock.TestContainer
import com.hana.sns.post.controller.request.PostCreateRequest
import com.hana.sns.post.controller.request.PostModifyRequest
import com.hana.sns.post.controller.response.PostResponse
import com.hana.sns.post.domain.Post
import com.hana.sns.post.service.PostServiceImpl
import com.hana.sns.user.controller.response.UserJoinResponse
import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.en.UserRole
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.lang.NullPointerException

class PostControllerTest {


    @Test
    fun 글작성_성공 () {
        //given
        val testContainer = TestContainer.build()
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        userRepository.save(User("userName",passwordEncoder.encode("password")))

        val postCreateRequest = PostCreateRequest("title", "body")
        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when
        val result = testContainer.postController.create(postCreateRequest, authentication)

        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result).isEqualTo(1)
    }

    @Test
    fun 글작성시_없는userName이라면_예외발생 () {
        //given
        val testContainer = TestContainer.build()
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        userRepository.save(User("userName",passwordEncoder.encode("password")))

        val postCreateRequest = PostCreateRequest("title", "body")
        val authentication = TestingAuthenticationToken("wrongUserName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when
        val result = assertThrows<SnsApplicationException> { testContainer.postController.create(postCreateRequest, authentication) }.errorCode

        //then
        assertThat(result).isEqualTo(ErrorCode.USER_NOT_FOUND)

    }

    @Test
    fun 글_수정이_성공하는_경우() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val result: Response<PostResponse> = postController.modify(savedPost.id!!, postModifyRequest , authentication)

        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result.title).isEqualTo(modifyTitle)
        assertThat(result.result.body).isEqualTo(modifyBody)
        assertThat(result.result.user.userName).isEqualTo(savedUser.userName)
    }

    @Test
    fun 로그인하지_않은_유저가_글_수정을_하는_경우_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        //when & then
        assertThrows<NullPointerException>  { postController.modify(savedPost.id!!, postModifyRequest , null!!) }
    }
    @Test
    fun 글_수정시_본인이_작성한_글이_아니라면_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        val authentication = TestingAuthenticationToken("wrongUserName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val errorCode = assertThrows<SnsApplicationException> {
            postController.modify(savedPost.id!!, postModifyRequest , authentication)
        }.errorCode

        //then
        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PERMISSION)
    }
    @Test
    fun 글_수정시_수정하려는_글이_없는경우_예외를_발생한다() {
        //given
        val testContainer = TestContainer.build()
        val postController = testContainer.postController
        val userRepository = testContainer.userRepository
        val postRepository = testContainer.postRepository
        val passwordEncoder = testContainer.passwordEncoder

        val user = User.fixture("userName",passwordEncoder.encode("password"))
        val post = Post.fixture("title","body",user)

        val savedUser: User = userRepository.save(user)
        val savedPost: Post = postRepository.save(post)

        val modifyTitle = "modifyTitle"
        val modifyBody = "modifyBody"
        val postModifyRequest = PostModifyRequest(modifyTitle, modifyBody)

        val authentication = TestingAuthenticationToken("userName","password", mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))
        //when
        val errorCode = assertThrows<SnsApplicationException> {
            postController.modify(9999, postModifyRequest , authentication)
        }.errorCode

        //then
        assertThat(errorCode).isEqualTo(ErrorCode.POST_NOT_FOUND)
    }
}
