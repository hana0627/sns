package com.hana.sns.common.controller

import com.hana.sns.common.controller.response.Response
import com.hana.sns.common.exception.SnsApplicationException
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


/**
 * 예외 catch하는 Exception Handler
 */
@RestControllerAdvice
class ControllerAdvice(
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(SnsApplicationException::class)
    fun applicationHandler(e: SnsApplicationException) : ResponseEntity<Any> {
        log.debug("Error occurs ${e.toString()}")
        return ResponseEntity.status(e.errorCode.status)
            .body(Response.error(e.errorCode.name))

    }

}
