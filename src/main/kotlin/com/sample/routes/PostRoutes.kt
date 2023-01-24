package com.sample.routes

import com.google.gson.Gson
import com.sample.data.requests.CreatePostRequest
import com.sample.data.requests.DeletePostRequest
import com.sample.data.responses.BasicApiResponse
import com.sample.routes.util.userId
import com.sample.service.CommentService
import com.sample.service.LikeService
import com.sample.service.PostService
import com.sample.util.ApiResponseMessages
import com.sample.util.Constants
import com.sample.util.QueryParams
import com.sample.util.save
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.io.File

fun Route.createPost(
    postService: PostService
) {
    val gson: Gson by inject()

    authenticate {
        post("/api/post/create") {
            val multipart = call.receiveMultipart()
            var createPostRequest: CreatePostRequest? = null
            var fileName: String? = null
            multipart.forEachPart { partData ->
                when (partData) {
                    is PartData.FormItem -> {
                        if (partData.name == "post_data") {
                            createPostRequest = gson.fromJson(
                                partData.value,
                                CreatePostRequest::class.java
                            )
                        }
                    }
                    is PartData.FileItem -> {
                        fileName = partData.save(Constants.POST_CONTENT_PATH)
                    }
                    else -> Unit
                }
            }

            val postContentUrl = "${Constants.BASE_URL}post_contents/$fileName"

            createPostRequest?.let { request ->
                val createPostAcknowledge = postService.createPost(
                    request = request,
                    userId = call.userId,
                    contentUrl = postContentUrl
                )
                if (createPostAcknowledge) {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse<Unit>(
                            successful = true,
                            message = fileName?.takeLastWhile { it != '.' }
                        )
                    )
                } else {
                    File("${Constants.POST_CONTENT_PATH}/$fileName").delete()
                    call.respond(
                        HttpStatusCode.InternalServerError
                    )
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
        }
    }
}

fun Route.getPostsForProfile(
    postService: PostService
) {
    authenticate {
        get("/api/user/post") {
            val userId = call
                .parameters[QueryParams.PARAM_USER_ID]
            val page = call
                .parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call
                .parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            val posts = postService.getPostForProfile(
                ownUserId = call.userId,
                userId = userId ?: call.userId,
                page = page,
                pageSize = pageSize
            )
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}

fun Route.getPostForFollows(
    postService: PostService
) {
    authenticate {
        get("/api/post/follow/get") {
            val page = call
                .parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call
                .parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            val posts = postService.getPostForFollows(call.userId, page, pageSize)
            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}

fun Route.getPostForLikes(
    postService: PostService
) {
    authenticate {
        get("/api/post/like/get") {
            val userId = call
                .parameters[QueryParams.PARAM_USER_ID]
            val page = call
                .parameters[QueryParams.PARAM_PAGE]?.toIntOrNull() ?: 0
            val pageSize = call
                .parameters[QueryParams.PARAM_PAGE_SIZE]?.toIntOrNull() ?: Constants.DEFAULT_POST_PAGE_SIZE

            val posts = postService.getPostForLike(
                ownUserId = call.userId,
                userId = userId ?: call.userId,
                page = page,
                pageSize = pageSize
            )

            call.respond(
                HttpStatusCode.OK,
                posts
            )
        }
    }
}

fun Route.deletePost(
    postService: PostService,
    likeService: LikeService,
    commentService: CommentService
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

            if (post.userId == call.userId) {
                postService.deletePost(postId = request.postId)
                likeService.deleteLikesForParent(parentId = request.postId)
                commentService.deleteCommentForPost(postId = request.postId)
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse<Unit>(
                        successful = true,
                        message = ApiResponseMessages.DELETE_POST_SUCCESSFULLY
                    )
                )
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
            }
        }
}

fun Route.getPostDetails(
    postService: PostService
) {
    get("/api/post/details") {
        val postId = call.parameters["postId"] ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }
        val post = postService.getPostDetails(call.userId, postId) ?: kotlin.run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }
        call.respond(
            HttpStatusCode.OK,
            BasicApiResponse(
                successful = true,
                data = post
            )
        )
    }
}
