package com.hana.sns.common.config.filter

import com.hana.sns.common.utils.JwtUtils
import com.hana.sns.user.controller.port.UserService
import com.hana.sns.user.domain.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

@RequiredArgsConstructor
class JwtFilter(
    private val key:String?,
    private val userService: UserService,
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(javaClass)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // get header
        val header: String? = request.getHeader(HttpHeaders.AUTHORIZATION)
        if(header == null || !header.startsWith("Bearer ")) {
            log.error("Error occurs while getting headers")
            filterChain.doFilter(request,response)
            return
        }

        try {
            val token: String = header.split(" ")[1].trim()
            if(key == null) {
                log.error("key is null")
                return
            }

            // check token is valid
            if (JwtUtils.isExpired(token, key)) {
                log.error("key is expired")
                filterChain.doFilter(request, response)
            }

            // get username from token
            val userName = JwtUtils.getUserName(token, key)

            // check the username is valid
            val user: User = userService.loadUserByUserName(userName)


            // authorization
            val auth = UsernamePasswordAuthenticationToken(
                user,null, user.authorities
            )
            auth.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = auth
        } catch (e: RuntimeException) {
            log.error("Error occurs while validation ${e.toString()}")
            return
        }
        filterChain.doFilter(request, response)

    }
}
