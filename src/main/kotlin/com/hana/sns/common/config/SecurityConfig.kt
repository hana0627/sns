package com.hana.sns.common.config

import com.hana.sns.common.config.filter.JwtFilter
import com.hana.sns.user.controller.port.UserService
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfig constructor (
    @Value("\${jwt.secret-key}")
    private val secretKey: String? = null
) {
    private lateinit var userService: UserService
    @Autowired
    fun init(userService: UserService) {
        this.userService = userService
    }
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { c -> c.disable() }
            .authorizeHttpRequests { auth -> auth
                .requestMatchers("/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/*/users/join", "/api/*/users/login").permitAll()
                .requestMatchers("/api/**").authenticated()
            }
            .sessionManagement { s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(JwtFilter(secretKey, userService), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

}
