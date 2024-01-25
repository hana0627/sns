package com.hana.sns.post.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.TestContainer
import com.hana.sns.post.controller.request.PostCreateRequest
import com.hana.sns.user.controller.response.UserJoinResponse
import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.en.UserRole
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.assertThat
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

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
}
