package com.sample.plugins

import com.sample.routes.*
import com.sample.service.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()
    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        // User Route
        createUser(userService)
        loginUser(
            userService,
            jwtIssuer,
            jwtAudience,
            jwtSecret
        )

        // Follow Route
        followUser(followService)
        unFollowUser(followService)

        // Post Route
        createPost(
            postService,
            userService
        )
        getPostForFollows(
            postService,
            userService
        )
        getPostByLikes(
            userService,
            postService,
            likeService
        )
        deletePost(
            postService,
            userService,
            likeService
        )

        // Like Route
        likeParent(
            likeService,
            userService
        )
        unlikeParent(
            userService,
            likeService
        )

        // Comment Route
        createComment(
            commentService,
            userService
        )
        getCommentsForPost(
            commentService
        )
        getCommentedPostsForUser(
            userService,
            commentService
        )
        deleteComment(
            commentService,
            userService,
            likeService
        )
    }
}
