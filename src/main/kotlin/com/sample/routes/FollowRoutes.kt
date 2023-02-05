package com.sample.routes

import com.sample.data.models.Activity
import com.sample.data.requests.FollowUpdateRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.data.util.ActivityType
import com.sample.routes.util.userId
import com.sample.service.ActivityService
import com.sample.service.FollowService
import com.sample.util.ApiResponseMessages
import com.sample.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUser(
    followService: FollowService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/following/follow") {
            val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val didUserExist = followService.followUserIfExists(request, call.userId)

            if (didUserExist) {
                activityService.createActivity(
                    Activity(
                        timestamp = System.currentTimeMillis(),
                        byUserId = call.userId,
                        toUserId = request.followedUserId,
                        type = ActivityType.FollowedUser.type,
                        parentId = ""
                    )
                )
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true,
                        message = ApiResponseMessages.FOLLOW_SUCCESSFULLY
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.unFollowUser(
    followService: FollowService
) {
    authenticate {
        delete("/api/following/unfollow") {
            val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val didUserExist = followService.unfollowUserIfExists(request, call.userId)

            if (didUserExist) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true,
                        message = ApiResponseMessages.UN_FOLLOW_SUCCESSFULLY
                    )
                )
            } else {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = false,
                        message = ApiResponseMessages.USER_NOT_FOUND
                    )
                )
            }
        }
    }
}

fun Route.getFollowers(
    followService: FollowService
) {
    authenticate {
        get("/api/follow/users/following") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            val users = followService.getFollowers(userId ?: call.userId)

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = users
                )
            )
        }
    }
}

fun Route.getFollowings(
    followService: FollowService
) {
    authenticate {
        get("/api/follow/users/followed") {
            val userId = call.parameters[QueryParams.PARAM_USER_ID]
            val users = followService.getFollowings(userId ?: call.userId)

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    data = users
                )
            )
        }
    }
}
