package com.hana.sns.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.user.controller.request.UserJoinRequest
import com.hana.sns.user.controller.request.UserLoginRequest
import com.hana.sns.user.model.User
import com.hana.sns.user.service.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val om: ObjectMapper,
    private val userService: UserService,
) {


    @Test
    fun 회원가입() {
        //given
        val userName: String = "username"
        val password: String = "password"

        Mockito.`when`(userService.join(userName,password)).thenReturn(mock(User::class.java))

        //when & then
        mockMvc.perform(
            post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(UserJoinRequest(userName = userName, password = password)))
        )
            .andDo { result -> println(result.response.contentAsString) }
            .andExpect { status().isOk }

    }


    @Test
    fun 회원가입시_이미_회원가입된_username으로_회원가입을_하는경우_에러반환() {
        //given
        val userName: String = "username"
        val password: String = "password"


        Mockito.`when`(userService.join(userName,password)).thenThrow(SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""))

        //when & then
        mockMvc.perform(
            post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(UserJoinRequest(userName = userName, password = password)))
        )
            .andDo { result -> println(result.response.contentAsString) }
            .andExpect { status().isConflict }

    }





    @Test
    fun  로그인() {
        //given
        val userName: String = "username"
        val password: String = "password"

        Mockito.`when`(userService.login(userName, password)).thenReturn("test-String")

        //when & then
        mockMvc.perform(
            post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(UserLoginRequest(userName = userName, password = password)))
        )
            .andDo { result -> println(result.response.contentAsString) }
            .andExpect { status().isOk }
    }




    @Test
    fun  로그인시_회원가입이_안된_userName으로_로그인을_입력할_경우_에러발생() {
        //given
        val userName: String = "username"
        val password: String = "password"

        Mockito.`when`(userService.login(userName, password)).thenThrow(SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"TODO"))

        //when & then
        mockMvc.perform(
            post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(UserLoginRequest(userName = userName, password = password)))
        )
            .andDo { result -> println(result.response.contentAsString) }
            .andExpect { status().isNotFound }
    }


    @Test
    fun  로그인시_틀린_password를_입력할_경우_에러발생() {
        //given
        val userName: String = "username"
        val password: String = "password"

        Mockito.`when`(userService.login(userName, password)).thenThrow(SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"TODO"))

        //when & then
        mockMvc.perform(
            post("/api/v1/users/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsBytes(UserLoginRequest(userName = userName, password = password)))
        )
            .andDo { result -> println(result.response.contentAsString) }
            .andExpect { status().isUnauthorized }
    }


}