package com.hana.sns

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class SnsApplication

fun main(args: Array<String>) {
    runApplication<SnsApplication>(*args)
}
