package com.hana.sns.common.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*

fun generateToken(userName: String, key: String, expiredMs: Long): String{
    val claims: Claims = Jwts.claims()
    claims.put("userName", userName)

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + expiredMs))
        .signWith(getKey(key),SignatureAlgorithm.HS256)
        .compact()

}

private fun getKey(key: String): Key {
    val keyByte: ByteArray  = key.toByteArray(StandardCharsets.UTF_8)
    return Keys.hmacShaKeyFor(keyByte)
}
