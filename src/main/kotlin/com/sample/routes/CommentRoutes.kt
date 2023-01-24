package com.sample.routes

import com.sample.data.requests.CreateCommentRequest
import com.sample.data.requests.DeleteCommentRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.routes.util.userId
import com.sample.service.ActivityService
import com.sample.service.CommentService
import com.sample.service.LikeService
import com.sample.util.ApiResponseMessages
import com.sample.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createComment(
    commentService: CommentService,
    activityService: ActivityService
) {
    authenticate {
        post("/api/comment/create") {
            val request = call.receiveNullable<CreateCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            val userId = call.userId
                when (commentService.createComment(request, userId)) {
                    is CommentService.ValidationEvent.ErrorFieldEmpty -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse<Unit>(
                                successful = false,
                                message = ApiResponseMessages.FIELDS_BLANK
                            )
                        )
                    }
                    is CommentService.ValidationEvent.ErrorCommentTooLong -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse<Unit>(
                                successful = false,
                                message = ApiResponseMessages.COMMENT_TOO_LONG
                            )
                        )
                    }
                    is CommentService.ValidationEvent.Success -> {
                        activityService.addCommentActivity(
                            byUserId = userId,
                            postId = request.postId
                        )
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse<Unit>(
                                successful = true,
                                message = ApiResponseMessages.CREATE_COMMENT_SUCCESSFULLY
                            )
                        )
                    }

                    CommentService.ValidationEvent.UserNotFound -> {
                        call.respond(
                            HttpStatusCode.OK,
                            BasicApiResponse<Unit>(
                                successful = false,
                                message = "User not found"
                            )
                        )
                    }
                }
        }
    }
}

fun Route.getCommentsForPost(
    commentService: CommentService
) {
    get("/api/comment/post/get") {
        val postId = call.parameters[QueryParams.PARAM_POST_ID] ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val comments = commentService.getCommentsForPost(
            postId,
            ownUserId = call.userId
        )
        call.respond(
            HttpStatusCode.OK,
            BasicApiResponse(
                successful = true,
                data = comments
            )
        )
    }
}

fun Route.deleteComment(
    commentService: CommentService,
    likeService: LikeService
) {
    authenticate {
        delete("api/comment/delete") {
            val request = call.receiveNullable<DeleteCommentRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val comment = commentService.getCommentById(request.commentId)
            if (comment?.userId != call.userId) {
                call.respond(HttpStatusCode.Unauthorized)
                return@delete
            }
                val deleted = commentService.deleteComment(request.commentId)

                if (deleted) {
                    likeService.deleteLikesForParent(request.commentId)
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                            message = ApiResponseMessages.DELETE_COMMENT_SUCCESSFULLY
                            )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        BasicApiResponse<Unit>(successful = false)
                    )
                }
            }
        }
    }
