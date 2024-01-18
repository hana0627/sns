package com.hana.sns.user.service

import com.hana.sns.common.exception.SnsApplicationException
import com.hana.sns.common.exception.en.ErrorCode
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

    fun join(userName: String, password: String): User {
        // 회원가입된 userName으로 회원 가입된 user가 있는지
        if (userEntityRepository.findByUserName(userName) != null) {
            throw SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "$userName is duplicated")
        }

        // 회원가입 진행 = user 등록
        val userEntity: UserEntity = userEntityRepository.save(UserEntity(userName, password))

        return User(userEntity);
    }

    fun login(userName: String, password: String): String {
        // 회원가입 여부 체크
        //TODO 임시 예외처리
        val userEntity: UserEntity  = userEntityRepository.findByUserName(userName)?:throw SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"TODO")

        // 비밀번호 체크
        if(userEntity.password != password) {
            //TODO 임시 예외처리
            throw SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,"TODO")
        }
        // 토큰 생성

        return ""
    }
}