package com.hana.sns.common.exception

import com.hana.sns.common.controller.response.Response
import com.hana.sns.common.exception.en.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        if (response != null) {
            response.contentType = "application/json"
            response.status = ErrorCode.INVALID_TOKEN.status.value()
            response.writer.write(Response.error(ErrorCode.INVALID_TOKEN.name).toStream());
        }
    }
}
