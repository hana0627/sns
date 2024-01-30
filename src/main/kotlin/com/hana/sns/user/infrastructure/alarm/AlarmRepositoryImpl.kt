package com.hana.sns.user.infrastructure.alarm

import com.hana.sns.user.domain.Alarm
import com.hana.sns.user.domain.User
import com.hana.sns.user.infrastructure.UserEntity
import com.hana.sns.user.service.port.AlarmRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
@RequiredArgsConstructor
class AlarmRepositoryImpl(
    private val alarmJpaRepository: AlarmJpaRepository,
) : AlarmRepository {

    override fun findAllByUser(user: User, pageable: Pageable): Page<Alarm> {
        return alarmJpaRepository.findAllByUser(UserEntity(user) ,pageable).map{ Alarm(it) }
    }

    override fun save(alarm: Alarm): Alarm {
        return Alarm(alarmJpaRepository.save(AlarmEntity(alarm)))
    }
}