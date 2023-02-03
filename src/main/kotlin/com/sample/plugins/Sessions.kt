package com.sample.plugins

import com.sample.service.chat.ChatSession
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlin.collections.set

fun Application.configureSessions() {
    install(Sessions) {
        cookie<ChatSession>("SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }
    intercept(Plugins) {
        if(call.sessions.get<ChatSession>() == null) {
            val userId = call.parameters["userId"] ?: return@intercept
            call.sessions.set(ChatSession(userId, generateNonce()))
        }
    }
}