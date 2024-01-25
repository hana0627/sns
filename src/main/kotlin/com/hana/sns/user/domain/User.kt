package com.hana.sns.user.domain

import com.hana.sns.user.infrastructure.UserEntity
import com.hana.sns.user.domain.en.UserRole
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@NoArgsConstructor
data class User (
    val userName: String,
    val _password: String,
    var userRole: UserRole? = null,
    var registeredAt: LocalDateTime? = null,
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
    var id: Int? = null,
    ) : UserDetails{

    constructor(entity: UserEntity): this(
            entity.userName,
            entity.password,
            entity.role,
            entity.registeredAt,
            entity.updatedAt,
            entity.deletedAt,
            entity.id,
        )


    companion object {
        fun fixture(
            userName: String ="userName",
            password: String ="password",
            userRole: UserRole? = null,
            registeredAt: LocalDateTime? = null,
            updatedAt: LocalDateTime? = null,
            deletedAt: LocalDateTime? = null,
            id: Int? = null,

        ) : User {
            return User(
                userName = userName,
                _password = password,
                userRole = userRole,
                registeredAt = registeredAt,
                updatedAt = updatedAt,
                deletedAt = deletedAt,
                id = id,
            )
        }
    }


    // 스프링 시큐리티 관련 //
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(this.userRole.toString()))
    }

    override fun getPassword(): String {
        return this._password
    }

    override fun getUsername(): String {
        return this.userName
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
