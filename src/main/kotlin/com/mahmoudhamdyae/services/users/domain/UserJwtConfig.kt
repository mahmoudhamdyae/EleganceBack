package com.mahmoudhamdyae.services.users.domain

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object UserJwtConfig {
    val SECRET = System.getenv("secret") ?: "secret"
    const val ISSUER = "http://0.0.0.0:8080/"
    const val AUDIENCE = "http://0.0.0.0:8080/task"
    const val REALM = "Access to 'hello'"
    const val REFRESH_TOKEN_LIFETIME = (30L * 24L * 60L * 60L * 1000L) // Almost Month
    const val ACCESS_TOKEN_LIFETIME = (60L * 60L * 1000L) // A Minute
}

fun generateToken(userId: String, tokenLifetime: Long): String =
    JWT.create()
        .withAudience(userJwtConfig.AUDIENCE)
        .withIssuer(userJwtConfig.ISSUER)
        .withClaim("userId", userId)
        .withExpiresAt(Date(System.currentTimeMillis() + tokenLifetime))
        .sign(Algorithm.HMAC256(userJwtConfig.SECRET))

val userJwtConfig = UserJwtConfig