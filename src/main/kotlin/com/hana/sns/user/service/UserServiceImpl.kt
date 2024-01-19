package com.hana.sns.user.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
import com.hana.sns.common.utils.generateToken
import com.hana.sns.user.controller.port.UserService
import com.hana.sns.user.infrastructure.UserEntity
import com.hana.sns.user.domain.User
import com.hana.sns.user.service.port.UserRepository
import lombok.Builder
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@RequiredArgsConstructor
class UserServiceImpl (
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
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
        val userEntity: User = userRepository.findByUserName(userName)?:throw SnsApplicationException(ErrorCode.USER_NOT_FOUND,"$userName is not founded")

        // 비밀번호 체크
        if(!passwordEncoder.matches(password, userEntity.password)) {
            throw SnsApplicationException(ErrorCode.INVALID_PASSWORD)
        }
        // 토큰 생성
        val result = generateToken(userName, secretKey, expiredMs)

        return result
    }
}