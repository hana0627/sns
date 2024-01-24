package com.hana.sns.post.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.FakePasswordEncoder
import com.hana.sns.mock.FakePostRepository
import com.hana.sns.mock.FakeUserRepository
import com.hana.sns.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PostServiceTest {

    @Test
    fun 글작성이_성공하는_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)
        val user = User.fixture("userName",passwordEncoder.encode("password"))
        userRepository.save(user)

        val title: String = "title"
        val body: String = "postBody"

        //when
        postService.create(title, body, user.userName)

        //then
        val result = postRepository.findById(1)
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.title).isEqualTo("title")
        assertThat(result?.body).isEqualTo("postBody")
    }
    @Test
    fun 글작성시_요청한_유저가_없는_경우() {
        //given
        val passwordEncoder = FakePasswordEncoder()
        val userRepository = FakeUserRepository()
        val postRepository = FakePostRepository()
        val postService = PostServiceImpl(postRepository, userRepository)

        val title: String = "title"
        val body: String = "postBody"

        //when & then
        val errorCode = assertThrows<SnsApplicationException>{
            postService.create(title, body, "emptyUser") }.errorCode

        println(ErrorCode.USER_NOT_FOUND)

    }
}
