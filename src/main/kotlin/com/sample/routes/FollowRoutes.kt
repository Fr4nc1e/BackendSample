package com.sample.routes

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.requests.FollowUpdateRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.util.ApiResponseMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.followUser(
    followRepository: FollowRepository
) {
    post("/api/following/follow") {
        val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val didUserExist = followRepository.followUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )

        if (didUserExist) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    message = ApiResponseMessages.FOLLOW_SUCCESSFULLY
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = ApiResponseMessages.USER_NOT_FOUND
                )
            )
        }
    }
}

fun Route.unFollowUser(
    followRepository: FollowRepository
) {
    delete("/api/following/unfollow") {
        val request = call.receiveNullable<FollowUpdateRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        val didUserExist = followRepository.unfollowUserIfExists(
            followingUserId = request.followingUserId,
            followedUserId = request.followedUserId
        )

        if (didUserExist) {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = true,
                    message = ApiResponseMessages.UN_FOLLOW_SUCCESSFULLY
                )
            )
        } else {
            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(
                    successful = false,
                    message = ApiResponseMessages.USER_NOT_FOUND
                )
            )
        }
    }
}
