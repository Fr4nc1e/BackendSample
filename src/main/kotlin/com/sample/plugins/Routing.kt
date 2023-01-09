package com.sample.plugins

import com.sample.routes.*
import com.sample.service.FollowService
import com.sample.service.PostService
import com.sample.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()
    val jwtIssuer = environment.config.property("jwt.domain").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtSecret = environment.config.property("jwt.secret").getString()

    routing {
        createUserRoute(userService)
        loginUser(
            userService,
            jwtIssuer,
            jwtAudience,
            jwtSecret
        )
        followUser(followService)
        unFollowUser(followService)
        createPostRoute(
            postService,
            userService
            )
    }
}
