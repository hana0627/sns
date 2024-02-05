package com.hana.sns.user.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.common.utils.JwtUtils
import com.hana.sns.user.controller.port.UserService
import com.hana.sns.user.controller.response.AlarmResponse
import com.hana.sns.user.domain.User
import com.hana.sns.user.service.port.AlarmRepository
import com.hana.sns.user.service.port.UserCacheRepository
import com.hana.sns.user.service.port.UserRepository
import lombok.NoArgsConstructor
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
@NoArgsConstructor
class UserServiceImpl (
    private val userRepository: UserRepository,
    private val alarmRepository: AlarmRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userCacheRepository: UserCacheRepository,
) : UserService{

    @Value("\${jwt.secret-key}")
    private val secretKey: String? = null
    @Value("\${jwt.token.expired-time-ms}")
    private val expiredMs: Long? = null


    @Transactional
    override fun join(userName: String, password: String): User {
        // 회원가입된 userName으로 회원 가입된 user가 있는지
        if (userRepository.findByUserName(userName) != null) {
            throw SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "$userName is duplicated")
        }

        // 회원가입 진행 = user 등록
        return userRepository.save(User(userName, passwordEncoder.encode(password)))
    }
    override fun login(userName: String, password: String): String {
        // 회원가입 여부 체크
        val user: User =  userCacheRepository.getUser(userName)
            ?:getUserByUserNameOrException(userName)
        userCacheRepository.setUser(user); // 유저정보가 있다면 캐싱

        // 비밀번호 체크
        if(!passwordEncoder.matches(password, user.password)) {
            throw SnsApplicationException(ErrorCode.INVALID_PASSWORD)
        }
        // 토큰 생성
        return JwtUtils.generateToken(userName, secretKey, expiredMs)
    }

    override fun loadUserByUserName(userName: String): User {
        return userCacheRepository.getUser(userName)
            ?:getUserByUserNameOrException(userName)
    }


    override fun getAlarms(user: User, pageable: Pageable): Page<AlarmResponse> {
        return alarmRepository.findAllByUserId(user.id!!, pageable).map { AlarmResponse(it) }
    }


    private fun getUserByUserNameOrException(userName: String) =
        userRepository.findByUserName(userName) ?: throw SnsApplicationException(
            ErrorCode.USER_NOT_FOUND,
            "$userName is not founded"
    )
}
