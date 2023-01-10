package com.sample.routes

import com.sample.data.requests.CreatePostRequest
import com.sample.data.requests.DeletePostRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.routes.util.ifEmailBelongsToUser
import com.sample.service.LikeService
import com.sample.service.PostService
import com.sample.service.UserService
import com.sample.util.ApiResponseMessages
import com.sample.util.Constants
import com.sample.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createPost(
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

            ifEmailBelongsToUser(
                userId = request.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
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
}

fun Route.getPostForFollows(
    postService: PostService,
    userService: UserService
) {
    authenticate {
        get {
            val userId = call.parameters[QueryParams.PARAM_USER_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val page = call.parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]
                ?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            ifEmailBelongsToUser(
                userId = userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                val posts = postService.getPostForFollows(
                    userId,
                    page,
                    pageSize
                )
                call.respond(
                    HttpStatusCode.OK,
                    posts
                )
            }
        }
    }
}

fun Route.deletePost(
    postService: PostService,
    userService: UserService,
    likeService: LikeService
) {
    authenticate {
        delete("/api/post/delete") {
            val request = call.receiveNullable<DeletePostRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@delete
            }

            val post = postService.getPost(request.postId)
            if (post == null) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }

            ifEmailBelongsToUser(
                userId = post.userId,
                validateEmail = userService::doesEmailBelongToUserId
            ) {
                postService.deletePost(postId = request.postId)
                likeService.deleteLikesForParent(parentId = request.postId)
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        message = ApiResponseMessages.DELETE_POST_SUCCESSFULLY
                    )
                )
            }
        }
    }
}
