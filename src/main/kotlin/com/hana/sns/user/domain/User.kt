package com.hana.sns.user.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.hana.sns.user.infrastructure.UserEntity
import com.hana.sns.user.domain.en.UserRole
import lombok.NoArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class User (
    val userName: String,
    val _password: String,
    val userRole: UserRole? = null,
    @JsonIgnore
    var registeredAt: LocalDateTime? = null,
    @JsonIgnore
    var updatedAt: LocalDateTime? = null,
    @JsonIgnore
    var deletedAt: LocalDateTime? = null,
    var id: Long? = null,
    ) : UserDetails{

    constructor(): this("","") // Json 역직렬화시 사용
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
            id: Long? = null,

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
    fun toEntity(): UserEntity {
        return UserEntity(userName, _password, userRole, registeredAt, updatedAt, deletedAt, id)
    }

    // 스프링 시큐리티 관련 //
    @JsonIgnore
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(this.userRole.toString()))
    }

    @JsonIgnore
    override fun getPassword(): String {
        return this._password
    }
    @JsonIgnore
    override fun getUsername(): String {
        return this.userName
    }

    @JsonIgnore
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isAccountNonLocked(): Boolean {
        return true
    }
    @JsonIgnore
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    @JsonIgnore
    override fun isEnabled(): Boolean {
        return true
    }
}
