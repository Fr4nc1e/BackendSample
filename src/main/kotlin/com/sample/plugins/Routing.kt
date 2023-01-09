package com.sample.plugins

import com.sample.data.repository.user.UserRepository
import com.sample.routes.*
import com.sample.service.FollowService
import com.sample.service.PostService
import com.sample.service.UserService
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val userService: UserService by inject()
    val followService: FollowService by inject()
    val postService: PostService by inject()

    routing {
        createUserRoute(userService)
        loginUser(userRepository)
        followUser(followService)
        unFollowUser(followService)
        createPostRoute(postService)
    }
}
