package com.sample.plugins

import com.sample.routes.*
import com.sample.service.FollowService
import com.sample.service.LikeService
import com.sample.service.PostService
import com.sample.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val likeService: LikeService by inject()
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
        deletePost(
            postService,
            userService,
            likeService
        )

        //Like Route
        likeParent(
            likeService,
            userService
        )
        unlikeParent(
            userService,
            likeService
        )
    }
}
