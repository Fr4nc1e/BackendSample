package com.sample.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.sample.data.models.User
import com.sample.data.requests.CreateAccountRequest
import com.sample.data.requests.LoginRequest
import com.sample.data.responses.AuthResponse
import com.sample.data.responses.BasicApiResponse
import com.sample.routes.util.userId
import com.sample.service.UserService
import com.sample.util.ApiResponseMessages.CREATE_USER_SUCCESSFULLY
import com.sample.util.ApiResponseMessages.FIELDS_BLANK
import com.sample.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.sample.util.ApiResponseMessages.USER_ALREADY_EXISTS
import com.sample.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.createUser(
    userService: UserService
) {
    route("/api/user/create") {
        post {
            val request = call.receiveNullable<CreateAccountRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userExists = userService.doesUserWithEmailExist(request.email)
            if (userExists) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = USER_ALREADY_EXISTS
                    )
                )
                return@post
            }

            if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
                call.respond(
                    BasicApiResponse(
                        successful = false,
                        message = FIELDS_BLANK
                    )
                )
                return@post
            }

            when (userService.validateCreateAccountRequest(request)) {
                is UserService.ValidationEvent.ErrorFieldEmpty -> {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = false,
                            message = FIELDS_BLANK
                        )
                    )
                }

                is UserService.ValidationEvent.Success -> {
                    userService.createUser(request)
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true,
                            message = CREATE_USER_SUCCESSFULLY
                        )
                    )
                }
            }
        }
    }
}

fun Route.loginUser(
    userService: UserService,
    jwtIssuer: String,
    jwtAudience: String,
    jwtSecret: String
) {
    post("/api/user/login") {
        val request = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = userService.getUserByEmail(request.email) ?: kotlin.run {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
            return@post
        }

        val isCorrectPassword = userService.isValidpassword(
            enteredPassword = request.password,
            actualPassword = user.password
        )

        if (isCorrectPassword) {
            val expiresIn = 1000L * 60L * 60L * 24L * 365L
            val token = JWT.create()
                .withClaim(
                    "userId",
                    user.id
                )
                .withIssuer(jwtIssuer)
                .withExpiresAt(
                    Date(
                        System.currentTimeMillis() + expiresIn
                    )
                )
                .withAudience(jwtAudience)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(
                HttpStatusCode.OK,
                AuthResponse(token = token)
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = INVALID_CREDENTIALS
                )
            )
        }
    }
}

fun Route.searchUser(
    userService: UserService
) {
    authenticate {
        get("/api/user/search") {
            val query = call.parameters[QueryParams.PARAM_QUERY]
            if (query.isNullOrBlank()) {
                call.respond(
                    HttpStatusCode.OK,
                    listOf<User>()
                )
                return@get
            }

            val searchResults = userService.searchUser(query, call.userId)

            call.respond(
                HttpStatusCode.OK,
                searchResults
            )
        }
    }
}
