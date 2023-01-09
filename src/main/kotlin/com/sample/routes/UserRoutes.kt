package com.sample.routes

import com.sample.data.repository.user.UserRepository
import com.sample.data.requests.CreateAccountRequest
import com.sample.data.requests.LoginRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.service.UserService
import com.sample.util.ApiResponseMessages.CREATE_USER_SUCCESSFULLY
import com.sample.util.ApiResponseMessages.FIELDS_BLANK
import com.sample.util.ApiResponseMessages.INVALID_CREDENTIALS
import com.sample.util.ApiResponseMessages.LOGIN_IN_SUCCESSFULLY
import com.sample.util.ApiResponseMessages.USER_ALREADY_EXISTS
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUserRoute(
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

fun Route.loginUser(userRepository: UserRepository) {
    post("/api/user/login") {
        val request = call.receiveNullable<LoginRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        if (request.email.isBlank() || request.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val isCorrectPassword = userRepository.doesPasswordForUserMatch(
            email = request.email,
            enteredPassword = request.password
        )

        if (isCorrectPassword) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    message = LOGIN_IN_SUCCESSFULLY
                )
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
