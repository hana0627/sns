package com.hana.sns.user.model

import lombok.NoArgsConstructor

@NoArgsConstructor
data class User (
    private val userName: String,
    private val password: String,

){

}