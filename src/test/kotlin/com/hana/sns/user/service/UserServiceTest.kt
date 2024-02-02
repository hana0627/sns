package com.hana.sns.user.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.mock.FakeAlarmRepository
import com.hana.sns.mock.FakePasswordEncoder
import com.hana.sns.mock.FakePostRepository
import com.hana.sns.mock.FakeUserRepository
import com.hana.sns.post.domain.Post
import com.hana.sns.user.controller.port.UserService
import com.hana.sns.user.domain.Alarm
import com.hana.sns.user.domain.User
import com.hana.sns.user.domain.arg.AlarmArgs
import com.hana.sns.user.domain.en.AlarmType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.PageRequest
import org.springframework.test.util.ReflectionTestUtils


class UserServiceTest() {

    private lateinit var userRepository: FakeUserRepository
    private lateinit var alarmRepository: FakeAlarmRepository
    private lateinit var passwordEncoder: FakePasswordEncoder
    private lateinit var userService: UserService


    @BeforeEach
    fun setUp() {
        userRepository = FakeUserRepository()
        alarmRepository = FakeAlarmRepository()
        passwordEncoder = FakePasswordEncoder()
        userService = UserServiceImpl(userRepository,alarmRepository, passwordEncoder)
    }

    @Test
    fun 회원가입이_정상적으로_동작하는_경우() {
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

        val postRepository = FakePostRepository()
        val post = Post.fixture("title","body",user1)
        val savedPost = postRepository.save(post)


        alarmRepository.save(Alarm.fixture(user1,AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser2.id!!,savedUser1.id!!, savedPost.id!!)))
        alarmRepository.save(Alarm.fixture(user1,AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser3.id!!,savedUser1.id!!, savedPost.id!!)))
        alarmRepository.save(Alarm.fixture(user1,AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser4.id!!,savedUser1.id!!, savedPost.id!!)))

        val pageable = PageRequest.of(0,10)
        //when
        val result = userService.getAlarms(user1,pageable).toList()

        //then
        assertThat(result.size).isEqualTo(3)
        assertThat(result[0].alarmType).isEqualTo(AlarmType.NEW_COMMENT_ON_POST)
        assertThat(result[0].args.fromUserId).isEqualTo(savedUser2.id)
        assertThat(result[0].args.targetUserId).isEqualTo(savedUser1.id)
        assertThat(result[0].args.targetPostId).isEqualTo(savedPost.id)
    }

    // Controller Authentication에서 검증
//    @Test
//    fun 유효하지_않은_유저가_알람을_요청할_시_예외발생() {
//        //given
//        val user1 = User.fixture("userName1", passwordEncoder.encode("password"))
//        val user2 = User.fixture("userName2", passwordEncoder.encode("password"))
//        val savedUser1 = userRepository.save(user1)
//        val savedUser2 = userRepository.save(user2)
//
//        val postRepository = FakePostRepository()
//        val post = Post.fixture("title","body",user1)
//        val savedPost = postRepository.save(post)
//
//        alarmRepository.save(Alarm.fixture(user1,AlarmType.NEW_COMMENT_ON_POST, AlarmArgs(savedUser2.id!!,savedUser1.id!!, savedPost.id!!)))
//
//        val pageable = PageRequest.of(0,10)
//        //when & then
//        val result = assertThrows<SnsApplicationException> { userService.getAlarms(user2,pageable) }
//
//        assertThat(result.errorCode).isEqualTo(ErrorCode.USER_NOT_FOUND)
//        assertThat(result.message).isEqualTo("invalidUser is not founded")
//    }
}

