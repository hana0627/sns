package com.hana.sns.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.NoArgsConstructor

@Entity
@Table
class UserEntity (

    @Column(name = "user_name")
    val userName: String,

    val password: String,

    @Id
    val id: Long? = null
){
}