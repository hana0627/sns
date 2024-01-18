package com.hana.sns.user.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.fixture.UserEntityFixture
import com.hana.sns.user.domain.UserEntity
import com.hana.sns.user.repository.UserEntityRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    @MockBean
    private val userEntityRepository: UserEntityRepository,
    @MockBean
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    @Test
    fun 회원가입이_정상적으로_동작하는_경우() {

        //given
        val userName: String = "userName"
        val password: String = "password"

        //when
        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(null)
        Mockito.`when`(passwordEncoder.encode(password)).thenReturn("encrytedPassword")
        Mockito.`when`(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(userName,password))

        //then
        assertDoesNotThrow { userService.join(userName, password) }

    }

    @Test
    fun 회원가입시_중복된_userName이_있는경우() {

        //given
        val userName: String = "userName"
        val password: String = "password"
        val fixture:UserEntity? = UserEntityFixture.get(userName, password)


        //when
        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(fixture)
        Mockito.`when`(passwordEncoder.encode(password)).thenReturn("encrytedPassword")
        Mockito.`when`(userEntityRepository.save(any())).thenReturn(mock<UserEntity?>(fixture))
        //then
        assertThrows<SnsApplicationException> {userService.join(userName=userName, password=password) }
    }


    @Test
    fun 로그인이_정상적으로_동작하는_경우() {
        //given
        val userName: String = "userName"
        val password: String = "password"
        val fixture:UserEntity? = UserEntityFixture.get(userName, password)

        //when
        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(fixture)
        if (fixture != null) {
            Mockito.`when`(passwordEncoder.matches(password,fixture.password)).thenReturn(true)
        }

        //then
        assertDoesNotThrow {userService.login(userName=userName, password=password) }
    }

    @Test
    fun 로그인시_userName으로_회원가입한_유저가_없는_경우() {

        //given
        val userName: String = "userName"
        val password: String = "password"

        //when
        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(null)

        //then
        assertThrows<SnsApplicationException> {userService.join(userName=userName, password=password) }
    }
    
    @Test
    fun 로그인시_패스워드가_틀린_경우() {

        //given
        val userName: String = "userName"
        val password: String = "password"
        val wrongPassword: String = "wrongPassword"
        val fixture:UserEntity? = UserEntityFixture.get(userName, password)


        //when
        Mockito.`when`(userEntityRepository.findByUserName(userName)).thenReturn(null)

        //then
        assertThrows<SnsApplicationException> {userService.join(userName=userName, password=wrongPassword) }
    }
}

