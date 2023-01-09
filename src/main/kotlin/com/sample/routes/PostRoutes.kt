package com.sample.routes

import com.sample.data.requests.CreatePostRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.service.PostService
import com.sample.service.UserService
import com.sample.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPostRoute(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        post("/api/post/create") {
            val request = call.receiveNullable<CreatePostRequest>() ?: kotlin.run {
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            val email = call.principal<JWTPrincipal>()?.getClaim(
                "email", String::class
            )
            val isEmailByUser = userService.doesEmailBelongToUserId(
                email = email ?: "",
                userId = request.userId
            )
            if (!isEmailByUser) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponseMessages.UNAUTHORIZED
                )
                return@post
            }

            val didUserExist = postService.createPostIfUserExists(request)

            if (!didUserExist) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        message = ApiResponseMessages.CREATE_POST_SUCCESSFULLY
                    )
                )
            }
        }
    }
}