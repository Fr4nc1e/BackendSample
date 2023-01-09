package com.sample.plugins

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.repository.user.UserRepository
import com.sample.routes.createUserRoute
import com.sample.routes.followUser
import com.sample.routes.loginUser
import com.sample.routes.unFollowUser
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()
    val followRepository: FollowRepository by inject()

    routing {
        createUserRoute(userRepository)
        loginUser(userRepository)
        followUser(followRepository)
        unFollowUser(followRepository)
    }
}
