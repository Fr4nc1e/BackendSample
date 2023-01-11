package com.sample.routes

import com.sample.data.requests.LikeUpdateRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.data.util.ParentType
import com.sample.routes.util.userId
import com.sample.service.ActivityService
import com.sample.service.LikeService
import com.sample.util.ApiResponseMessages
import com.sample.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.likeParent(
    likeService: LikeService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/like") {
            val request = call.receiveNullable<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
        return@post
    }
            val userId = call.userId
            val likeSuccessful = likeService.likeParent(
                userId = userId,
                parentId = request.parentId,
                parentType = request.parentType
            )
            if (likeSuccessful) {
                activityService.addLikeActivity(
                    byUserId = userId,
                    parentType = ParentType.fromType(request.parentType),
                    parentId = request.parentId
                )
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

fun Route.unlikeParent(
    likeService: LikeService
) {
    authenticate {
        delete("/api/unlike") {
            val request = call.receiveNullable<LikeUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
        return@delete
    }

            val unlikeParentSuccessful = likeService.unlikeParent(
                userId = call.userId,
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

fun Route.getLikesForParent(likeService: LikeService) {
    authenticate {
        get("/api/like/users") {
            val parentId = call.parameters[QueryParams.PARAM_PARENT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val usersLikedParent = likeService.getUsersLikedParent(
                userId = call.userId,
                parentId = parentId
            )
            call.respond(
                HttpStatusCode.OK,
                usersLikedParent
            )
        }
    }
}
