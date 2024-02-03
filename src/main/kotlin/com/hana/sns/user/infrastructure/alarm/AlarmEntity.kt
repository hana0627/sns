package com.hana.sns.user.infrastructure.alarm

import com.hana.sns.user.domain.Alarm
import com.hana.sns.user.domain.arg.AlarmArgs
import com.hana.sns.user.domain.en.AlarmType
import com.hana.sns.user.infrastructure.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime


@Entity
@Table(name = "alarm")
@SQLDelete(sql = "UPDATE alram SET deleted_at = NOW(6) WHERE id=?")
@SQLRestriction("deleted_at is NULL")
class AlarmEntity (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: UserEntity,

    @Enumerated(EnumType.STRING)
    val alarmType: AlarmType,

    @Column(name = "args", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    val alarmArgs: AlarmArgs,

    @Column(name = "registered_at")
    var registeredAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,


    ){

    @PrePersist
    fun registeredAt() {
        this.registeredAt = LocalDateTime.now();
    }

    @PreUpdate
    fun updatedAt() {
        this.updatedAt = LocalDateTime.now();
    }


}