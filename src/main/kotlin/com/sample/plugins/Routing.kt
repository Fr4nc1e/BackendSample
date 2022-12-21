package com.sample.plugins

import com.sample.data.repository.user.UserRepository
import com.sample.routes.createUserRoute
import com.sample.routes.loginUser
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val userRepository: UserRepository by inject()

    routing {
        createUserRoute(userRepository)
        loginUser(userRepository)
    }
}
