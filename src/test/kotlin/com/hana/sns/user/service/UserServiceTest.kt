package com.hana.sns.user.service

import com.hana.sns.mock.FakePasswordEncoder
import com.hana.sns.mock.FakeUserRepository
import com.hana.sns.user.domain.User
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test


class UserServiceTest  () {

    @Test
    fun 회원가입이_정상적으로_동작하는_경우() {

        //given
        val userName: String = "userName"
        val password: String = "password"
        val user = User.fixture(userName,password)


        val fakeUserRepository = FakeUserRepository()
        val fakePasswordEncoder = FakePasswordEncoder()

        val userService = UserServiceImpl(fakeUserRepository,fakePasswordEncoder)

        userService.join(user.userName, user.password)

        //then
        val result = fakeUserRepository.findByUserName("userName")
        Assertions.assertThat(result).isNotNull
            Assertions.assertThat(fakePasswordEncoder.matches(password, result?.password)).isTrue()

    }

//    @Test
//    fun 회원가입시_중복된_userName이_있는경우() {
//
//        //given
//        val userName: String = "userName"
//        val password: String = "password"
//        val fixture: UserEntity? = UserEntityFixture.get(userName, password)
//
//
//        //when
//        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(fixture)
//        Mockito.`when`(passwordEncoder.encode(password)).thenReturn("encrytedPassword")
//        Mockito.`when`(userEntityRepository.save(any())).thenReturn(mock<UserEntity?>(fixture))
//        //then
//        assertThrows<SnsApplicationException> {userService.join(userName=userName, password=password) }
//    }
//
//
//    @Test
//    fun 로그인이_정상적으로_동작하는_경우() {
//        //given
//        val userName: String = "userName"
//        val password: String = "password"
//        val fixture: UserEntity? = UserEntityFixture.get(userName, password)
//
//        //when
//        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(fixture)
//        if (fixture != null) {
//            Mockito.`when`(passwordEncoder.matches(password,fixture.password)).thenReturn(true)
//        }
//
//        //then
//        assertDoesNotThrow {userService.login(userName=userName, password=password) }
//    }
//
//    @Test
//    fun 로그인시_userName으로_회원가입한_유저가_없는_경우() {
//
//        //given
//        val userName: String = "userName"
//        val password: String = "password"
//
//        //when
//        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(null)
//
//        //then
//        assertThrows<SnsApplicationException> {userService.join(userName=userName, password=password) }
//    }
//
//    @Test
//    fun 로그인시_패스워드가_틀린_경우() {
//
//        //given
//        val userName: String = "userName"
//        val password: String = "password"
//        val wrongPassword: String = "wrongPassword"
//        val fixture: UserEntity? = UserEntityFixture.get(userName, password)
//
//
//        //when
//        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(null)
//
//        //then
//        assertThrows<SnsApplicationException> {userService.join(userName=userName, password=wrongPassword) }
//    }
}

