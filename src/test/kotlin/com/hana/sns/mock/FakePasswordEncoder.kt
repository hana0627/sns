package com.hana.sns.mock

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class FakePasswordEncoder(strength: Int) : BCryptPasswordEncoder(strength) {

    constructor() : this(10)

    override fun encode(rawPassword: CharSequence?): String {
        return super.encode(rawPassword)
    }

    override fun matches(rawPassword: CharSequence?, encodedPassword: String?): Boolean {
        return super.matches(rawPassword, encodedPassword)
    }
}
