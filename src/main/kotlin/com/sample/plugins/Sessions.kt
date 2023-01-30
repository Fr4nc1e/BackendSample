package com.sample.plugins

import com.sample.service.chat.ChatSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlin.collections.set

fun Application.configureSessions() {
    install(Sessions) {
        cookie<ChatSession>("SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
}