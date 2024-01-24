package com.hana.sns.user.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.FakePasswordEncoder
import com.hana.sns.mock.FakeUserRepository
import com.hana.sns.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.util.ReflectionTestUtils


class UserServiceTest() {

    @Test
    fun 회원가입이_정상적으로_동작하는_경우() {
        val userRepository = FakeUserRepository()
        val passwordEncoder = FakePasswordEncoder()
        val userService = UserServiceImpl(userRepository, passwordEncoder)


        //given
        val userName: String = "userName"
        val password: String = "password"

        //when
        userService.join(userName, password)

        //then
        val result = userRepository.findByUserName("userName")
        assertThat(result).isNotNull
        assertThat(passwordEncoder.matches(password, result?.password)).isTrue()

    }

    @Test
    fun 회원가입시_중복된_userName이_있는경우() {
        val userRepository = FakeUserRepository()
        val passwordEncoder = FakePasswordEncoder()
        val userService = UserServiceImpl(userRepository, passwordEncoder)

        //given
        val userName: String = "userName"
        val password: String = "password"
        val user = User.fixture("userName", "password123")
        userRepository.save(user)


        //when & then
        val errorCode = assertThrows<SnsApplicationException> { userService.join(userName, password) }.errorCode
        assertThat(errorCode).isEqualTo(ErrorCode.DUPLICATED_USER_NAME)
    }


    @Test
    fun 로그인이_정상적으로_동작하는_경우() {
        val userRepository = FakeUserRepository()
        val passwordEncoder = FakePasswordEncoder()
        val userService = UserServiceImpl(userRepository, passwordEncoder)

        //given
        val userName: String = "userName"
        val password: String = "password"
        val user = User.fixture(userName, passwordEncoder.encode(password))
        userRepository.save(user)


        // ReflectionTestUtils를 사용하여 private 필드에 값을 주입
        ReflectionTestUtils.setField(userService, "secretKey", "hanatestcode.abcdefghijklmn.secret_key")
        ReflectionTestUtils.setField(userService, "expiredMs", 360000L)

        //when
        val token = userService.login(userName, password)
        assertThat(token.startsWith("Bearer ")).isTrue()
    }

    @Test
    fun 로그인시_userName으로_회원가입한_유저가_없는_경우() {
        val userRepository = FakeUserRepository()
        val passwordEncoder = FakePasswordEncoder()
        val userService = UserServiceImpl(userRepository, passwordEncoder)

        //given
        val userName: String = "userName"
        val password: String = "password"
        val user = User.fixture(userName, passwordEncoder.encode(password))
        userRepository.save(user)

        //when & then
        val errorCode = assertThrows<SnsApplicationException> {
            userService.login("userName2222", "password")
        }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
    }

    @Test
    fun 로그인시_패스워드가_틀린_경우() {
        val userRepository = FakeUserRepository()
        val passwordEncoder = FakePasswordEncoder()
        val userService = UserServiceImpl(userRepository, passwordEncoder)

        //given
        val userName: String = "userName"
        val password: String = "password"
        val user = User.fixture(userName, passwordEncoder.encode(password))
        userRepository.save(user)

        //when & then
        val errorCode = assertThrows<SnsApplicationException> {
            userService.login("userName", "wrongPassword")
        }.errorCode

        assertThat(errorCode).isEqualTo(ErrorCode.INVALID_PASSWORD)
    }
}

