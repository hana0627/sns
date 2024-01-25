package com.hana.sns.user.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.TestContainer
import com.hana.sns.user.controller.request.UserJoinRequest
import com.hana.sns.user.controller.request.UserLoginRequest
import com.hana.sns.user.controller.response.UserJoinResponse
import com.hana.sns.user.controller.response.UserLoginResponse
import com.hana.sns.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.util.ReflectionTestUtils

class UserControllerTest(
) {

    @Test
    fun 회원가입() {
        val testContainer = TestContainer.build()
        val userController = testContainer.userController

        //given
        val userName: String = "userName"
        val password: String = "password"
        val userJoinRequest = UserJoinRequest(userName, password)

        //when
        val result: Response<UserJoinResponse> = userController.join(userJoinRequest)

        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result.userName).isEqualTo("userName")

    }


    @Test
    fun 회원가입시_이미_회원가입된_username으로_회원가입을_하는경우_에러반환() {
        val testContainer = TestContainer.build()
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        //given
        val userName: String = "userName"
        val password: String = "password"
        val user = User.fixture(userName, passwordEncoder.encode(password))

        userRepository.save(user)
        val userJoinRequest = UserJoinRequest("userName", "password11")

        //then & then
        val errorCode =
            assertThrows<SnsApplicationException> { testContainer.userController.join(userJoinRequest) }.errorCode
        assertThat(errorCode).isEqualTo(ErrorCode.DUPLICATED_USER_NAME)
    }


    @Test
    fun 로그인() {
        val testContainer = TestContainer.build()
        val userController = testContainer.userController
        val userService = testContainer.userService
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        //given
        val userName: String = "username"
        val password: String = "password"
        val user = User.fixture(userName, passwordEncoder.encode(password))

        userRepository.save(user)

        val loginRequest = UserLoginRequest(userName, password)

        // ReflectionTestUtils를 사용하여 private 필드에 값을 주입
        ReflectionTestUtils.setField(userService, "secretKey", "hanatestcode.abcdefghijklmn.secret_key")
        ReflectionTestUtils.setField(userService, "expiredMs", 360000L)

        //when & then
        val result: Response<UserLoginResponse> = userController.login(loginRequest)

        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result.token.startsWith("Bearer ")).isTrue()
    }


    @Test
    fun 로그인시_회원가입이_안된_userName으로_로그인을_입력할_경우_에러발생() {
        val testContainer = TestContainer.build()
        val userController = testContainer.userController

        //given
        val userName: String = "username"
        val password: String = "password"
        val loginRequest = UserLoginRequest(userName, password)

        //when & then
        val errorCode = assertThrows<SnsApplicationException> { userController.login(loginRequest) }.errorCode
        assertThat(errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
    }



    @Test
    fun 로그인시_틀린_password를_입력할_경우_에러발생() {
        val testContainer = TestContainer.build()
        val userController = testContainer.userController
        val userRepository = testContainer.userRepository
        val passwordEncoder = testContainer.passwordEncoder

        //given
        val userName: String = "username"
        val password: String = "password"
        val wrongPassword: String = "wrongPassword"
        val user = User.fixture(userName, passwordEncoder.encode(password))
        userRepository.save(user)

        val loginRequest = UserLoginRequest(userName, wrongPassword)

        //when & then
        val errorCode = assertThrows<SnsApplicationException> { userController.login(loginRequest) }.errorCode
        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PASSWORD)
    }


}