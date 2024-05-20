package com.mahmoudhamdyae.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mahmoudhamdyae.services.users.domain.userJwtConfig
import com.mahmoudhamdyae.services.users.domain.userRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {

    authentication {
        basic(name = "basic_auth") {
            validate { credentials ->
                val user =
                    userRepo.getUserByEmail(email = credentials.name.lowercase().trim()) ?: return@validate null
                val isPasswordValid =
                    userRepo.isUserPasswordValid(password = credentials.password, hashedPassword = user.password)

                if (isPasswordValid) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

        jwt(name = "jwt_auth") {
            realm = userJwtConfig.REALM
            verifier(
                JWT
                .require(Algorithm.HMAC256(userJwtConfig.SECRET))
                .withAudience(userJwtConfig.AUDIENCE)
                .withIssuer(userJwtConfig.ISSUER)
                .build())
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asString()
                val isUserIdExist = userRepo.isUserIdExist(userId = userId)
                if (isUserIdExist) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
