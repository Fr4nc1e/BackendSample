package com.sample.plugins

import com.sample.routes.*
import com.sample.service.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
    val commentService: CommentService by inject()
    val activityService: ActivityService by inject()
    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        // User Route
        authenticate()
        createUser(userService)
        loginUser(
            userService,
            jwtIssuer,
            jwtAudience,
            jwtSecret
        )
        searchUser(userService)
        getUserProfile(userService)
        updateUser(userService)

        // Follow Route
        followUser(
            followService,
            activityService
        )
        unFollowUser(followService)

        // Post Route
        createPost(postService)
        getPostForFollows(postService)
        getPostsForProfile(postService)
        getPostForLikes(postService)
        deletePost(
            postService,
            likeService,
            commentService
        )

        // Like Route
        likeParent(
            likeService,
            activityService
        )
        unlikeParent(likeService)
        getLikesForParent(likeService)

        // Comment Route
        createComment(
            commentService,
            activityService
        )
        getCommentsForPost(commentService)
        getCommentedPostsForUser(commentService)
        deleteComment(
            commentService,
            likeService
        )

        // Activity Route
        getActivities(activityService)

        static {
            resources("static")
        }
    }
}
