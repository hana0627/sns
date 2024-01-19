package com.hana.sns.common.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.security.Key
import java.util.*

fun getUserName(token: String, key: String): String {
    return extreactClaims(token, key).get("userName",String::class.java)
}

fun isExpired(token: String, key: String): Boolean {
    val expiredDate: Date = extreactClaims(token, key).expiration
    return expiredDate.before(Date())
}

private fun extreactClaims(token: String, key: String): Claims {
    return Jwts.parserBuilder().setSigningKey(getKey(key))
        .build().parseClaimsJws(token).body
}

fun generateToken(userName: String, key: String?, expiredMs: Long?): String{
    if(key == null || expiredMs == null) {
        throw NullPointerException("key 혹은 expiredMs가 존재하지 않습니다.")
    }

    val claims: Claims = Jwts.claims()
    claims.put("userName", userName)

    return "Bearer " + Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + expiredMs))
        .signWith(getKey(key), SignatureAlgorithm.HS256)
        .compact()

}

private fun getKey(key: String): Key {
    val keyByte: ByteArray  = key.toByteArray(StandardCharsets.UTF_8)
    return Keys.hmacShaKeyFor(keyByte)
}
