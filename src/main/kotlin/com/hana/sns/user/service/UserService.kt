package com.hana.sns.user.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.user.domain.UserEntity
import com.hana.sns.user.model.User
import com.hana.sns.user.repository.UserEntityRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class UserService (
    private val userEntityRepository: UserEntityRepository,
){

    // TODO : implement
    fun join(userName: String, password: String): User {
        // 회원가입된 userName으로 회원 가입된 user가 있는지
        userEntityRepository.findByUserName(userName)
        
        // 회원가입 진행 = user 등록
        userEntityRepository.save(UserEntity("A","B"))

        
        return User("A","B");
    }

    // TODO : implement
    fun login(userName: String, password: String): String {
        // 회원가입 여부 체크
        val userEntity: UserEntity  = userEntityRepository.findByUserName(userName)?:throw SnsApplicationException()

        // 비밀번호 체크
        if(userEntity.password != password) {
            throw SnsApplicationException()
        }
        // 토큰 생성

        return ""
    }
}