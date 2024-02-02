package com.hana.sns.user.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.FakePostRepository
import com.hana.sns.mock.TestContainer
import com.hana.sns.post.domain.Post
import com.hana.sns.post.service.port.PostRepository
import com.hana.sns.user.controller.request.UserJoinRequest
import com.hana.sns.user.controller.request.UserLoginRequest
import com.hana.sns.user.controller.response.UserJoinResponse
import com.hana.sns.user.controller.response.UserLoginResponse
import com.hana.sns.user.domain.Alarm
import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.arg.AlarmArgs
import com.hana.sns.user.domain.en.AlarmType
import com.hana.sns.user.domain.en.UserRole
import com.hana.sns.user.service.port.AlarmRepository
import com.hana.sns.user.service.port.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.util.ReflectionTestUtils

class UserControllerTest(
) {

    lateinit var testContainer: TestContainer
    lateinit var userRepository: UserRepository
    lateinit var postRepository: PostRepository
    lateinit var alarmRepository: AlarmRepository
    lateinit var passwordEncoder: PasswordEncoder
    lateinit var userController: UserController

    @BeforeEach
    fun setUp() {
        testContainer = TestContainer.build()
        userRepository = testContainer.userRepository
        postRepository = testContainer.postRepository
        alarmRepository = testContainer.alarmRepository
        passwordEncoder = testContainer.passwordEncoder
        userController = testContainer.userController
    }

    @Test
    fun 회원가입() {
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
        val userService = testContainer.userService
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

    @Test
    fun 알람기능() {
        //given
        val user1 = User.fixture("userName1", passwordEncoder.encode("password"))
        val user2 = User.fixture("userName2", passwordEncoder.encode("password"))
        val user3 = User.fixture("userName3", passwordEncoder.encode("password"))
        val user4 = User.fixture("userName4", passwordEncoder.encode("password"))
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val savedUser4 = userRepository.save(user4)

        val post = Post.fixture("title","body",user1)
        val savedPost = postRepository.save(post)

        alarmRepository.save(Alarm.fixture(user1, AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser2.id!!,savedUser1.id!!, savedPost.id!!)))
        alarmRepository.save(Alarm.fixture(user1, AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser3.id!!,savedUser1.id!!, savedPost.id!!)))
        alarmRepository.save(Alarm.fixture(user1, AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser4.id!!,savedUser1.id!!, savedPost.id!!)))


        val pageable = PageRequest.of(0,10)
        val authentication = TestingAuthenticationToken(user1,user1.password, mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        //when
        val result = userController.alarm(pageable,authentication)

        //then
        assertThat(result.resultCode).isEqualTo("SUCCESS")
        assertThat(result.result.toList().size).isEqualTo(3)
        assertThat(result.result.toList()[0].alarmType).isEqualTo(AlarmType.NEW_COMMENT_ON_POST)
        assertThat(result.result.toList()[0].args.targetUserId).isEqualTo(savedUser1.id)
        assertThat(result.result.toList()[0].args.fromUserId).isEqualTo(savedUser2.id)
        assertThat(result.result.toList()[0].args.targetPostId).isEqualTo(savedPost.id)
        assertThat(result.result.toList()[0].text).isEqualTo(AlarmType.NEW_COMMENT_ON_POST.alarmText)
    }

    @Test
    @WithAnonymousUser
    fun 로그인하지_않은_유저가_알람요청시_예외를_발생한다() {
        //given
        val user1 = User.fixture("userName1", passwordEncoder.encode("password"))
        val user2 = User.fixture("userName2", passwordEncoder.encode("password"))
        val user3 = User.fixture("userName3", passwordEncoder.encode("password"))
        val user4 = User.fixture("userName4", passwordEncoder.encode("password"))
        val savedUser1 = userRepository.save(user1)
        val savedUser2 = userRepository.save(user2)
        val savedUser3 = userRepository.save(user3)
        val savedUser4 = userRepository.save(user4)

        val post = Post.fixture("title","body",user1)
        val savedPost = postRepository.save(post)

        alarmRepository.save(Alarm.fixture(user1, AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser2.id!!,savedUser1.id!!, savedPost.id!!)))
        alarmRepository.save(Alarm.fixture(user1, AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser3.id!!,savedUser1.id!!, savedPost.id!!)))
        alarmRepository.save(Alarm.fixture(user1, AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser4.id!!,savedUser1.id!!, savedPost.id!!)))

        val authentication = TestingAuthenticationToken(null, null, mutableListOf(SimpleGrantedAuthority(UserRole.USER.toString())))

        val pageable = PageRequest.of(0,10)
        //when & then
        assertThrows<NullPointerException> {  userController.alarm(pageable,authentication) }

    }

}
