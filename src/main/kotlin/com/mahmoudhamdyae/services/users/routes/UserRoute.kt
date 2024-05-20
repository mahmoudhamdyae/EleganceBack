package com.mahmoudhamdyae.services.users.routes

import com.mahmoudhamdyae.services.users.domain.generateToken
import com.mahmoudhamdyae.services.users.domain.userJwtConfig
import com.mahmoudhamdyae.services.users.domain.userRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute() {
    route(path = "/user") {

        // Register a new user
        post(path = "/register") {
            val userFormParam = call.receiveParameters()

            val email = userFormParam["email"]?.lowercase()?.trim() ?: return@post call.respondText(
                text = "Missing email address",
                status = HttpStatusCode.BadRequest
            )

            val isEmailExist = userRepo.isUserEmailExist(email)

            if (isEmailExist) return@post call.respondText(
                text = "the email address already used!",
                status = HttpStatusCode.BadRequest
            )

            val password = userFormParam["password"]?.lowercase()?.trim() ?: return@post call.respondText(
                text = "Missing password",
                status = HttpStatusCode.BadRequest
            )

            val user = userRepo.insertUser(email = email, password = password)

            if (user != null) {
                val accessToken = generateToken(userId = user.userId, userJwtConfig.ACCESS_TOKEN_LIFETIME)
                val refreshToken = generateToken(userId = user.userId, userJwtConfig.REFRESH_TOKEN_LIFETIME)

                call.respond(
                    message = hashMapOf(
                        "userId" to user.userId,
                        "accessToken" to accessToken,
                        "refreshToken" to refreshToken
                    ),
                    status = HttpStatusCode.OK
                )
            } else {
                return@post call.respondText(
                    text = "Failed to add the user",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        // Login a user
        authenticate ("basic_auth") {
            post(path = "/login") {
                // Checking Email is responsibility of Basic Authentication
                val email = call.principal<UserIdPrincipal>()?.name!!

                val user = userRepo.getUserByEmail(email = email)
                    ?: return@post call.respondText(
                        text = "the email address $email is not exist!",
                        status = HttpStatusCode.BadRequest
                    )

                val accessToken = generateToken(userId = user.userId, userJwtConfig.ACCESS_TOKEN_LIFETIME)
                val refreshToken = generateToken(userId = user.userId, userJwtConfig.REFRESH_TOKEN_LIFETIME)

                call.respond(
                    message = hashMapOf(
                        "userId" to user.userId,
                        "accessToken" to accessToken,
                        "refreshToken" to refreshToken
                    ),
                    status = HttpStatusCode.OK
                )

//                val userFormParam = call.receiveParameters()
//
//                val email = userFormParam["email"]?.lowercase()?.trim() ?: return@post call.respondText(
//                    text = "Missing email address",
//                    status = HttpStatusCode.BadRequest
//                )
//
//                val password = userFormParam["password"]?.lowercase()?.trim() ?: return@post call.respondText(
//                    text = "Missing password",
//                    status = HttpStatusCode.BadRequest
//                )
//
//                val user = userRepo.getUserByEmail(email) ?: return@post call.respondText(
//                    text = "Missing password",
//                    status = HttpStatusCode.BadRequest
//                )
//
//                val isPasswordValid = userRepo.isUserPasswordValid(password, user.password)
//
//                if (!isPasswordValid) return@post call.respondText(
//                    text = "The password is incorrect",
//                    status = HttpStatusCode.BadRequest
//                )
//
//                call.respond(
//                    message = user.userId,
//                    status = HttpStatusCode.OK
//                )
            }
        }

        // Generate a new access token
        authenticate("jwt_auth") {
            get(path = "/refresh") {
                val principal = call.principal<JWTPrincipal>()
                // Confirming the userID existence is responsibility of JWT authentication
                val userId =
                    principal!!.payload.getClaim("userId").asString()

                val accessToken = generateToken(userId = userId, userJwtConfig.ACCESS_TOKEN_LIFETIME)

                call.respond(
                    message = hashMapOf("accessToken" to accessToken),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}