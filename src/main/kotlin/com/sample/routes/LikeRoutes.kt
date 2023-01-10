package com.sample.routes

import com.sample.data.requests.LikeUpdateRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.routes.util.ifEmailBelongsToUser
import com.sample.service.LikeService
import com.sample.service.UserService
import com.sample.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.likeParent(
    likeService: LikeService,
    userService: UserService
) {
    authenticate { post("/api/like") { val request = call.receiveNullable<LikeUpdateRequest>() ?: kotlin.run { call.respond(HttpStatusCode.BadRequest)
        return@post
    }
        ifEmailBelongsToUser(
            userId = request.userId,
            validateEmail = userService::doesEmailBelongToUserId
        ) {
            val likeSuccessful = likeService.likeParent(
                userId = request.userId,
                parentId = request.parentId
            )
            if (likeSuccessful) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        message = ApiResponseMessages.LIKE_SUCCESSFULLY
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.LIKE_UNSUCCESSFULLY
                    )
                )
            }
        }
    }
    }
}

fun Route.unlikeParent(
    userService: UserService,
    likeService: LikeService
) {
    authenticate { delete("/api/unlike") { val request = call.receiveNullable<LikeUpdateRequest>() ?: kotlin.run { call.respond(HttpStatusCode.BadRequest)
        return@delete
    }

        ifEmailBelongsToUser(
            request.userId,
            validateEmail = userService::doesEmailBelongToUserId
        ) {
            val unlikeParentSuccessful = likeService.unlikeParent(
                userId = request.userId,
                parentId = request.parentId
            )
            if (unlikeParentSuccessful) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        message = ApiResponseMessages.UNLIKE_SUCCESSFULLY
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = false,
                        message = ApiResponseMessages.UNLIKE_UNSUCCESSFULLY
                    )
                )
            }
        }
    }
    }
}
