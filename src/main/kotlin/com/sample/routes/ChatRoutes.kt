package com.sample.routes

import com.sample.data.responses.BasicApiResponse
import com.sample.routes.util.userId
import com.sample.service.chat.ChatController
import com.sample.service.chat.ChatService
import com.sample.service.chat.ChatSession
import com.sample.util.Constants
import com.sample.util.QueryParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach

fun Route.getMessagesForChat(chatService: ChatService) {
    authenticate {
        get("/api/chat/messages") {
            val chatId = call.parameters[QueryParams.PARAM_CHAT_ID] ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val page = call.parameters[QueryParams.PARAM_PAGE]
                ?.toIntOrNull() ?: 0
            val pageSize = call.parameters[QueryParams.PARAM_PAGE_SIZE]
                ?.toIntOrNull() ?: Constants.DEFAULT_MESSAGE_PAGE_SIZE

            if (!chatService.doesChatBelongToUser(call.userId, chatId)) {
                call.respond(HttpStatusCode.Forbidden)
                return@get
            }

            chatService.getMessagesForChat(
                chatId = chatId,
                page = page,
                pageSize = pageSize
            ).also {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(
                        successful = true,
                        data = it
                    )
                )
            }
        }
    }
}

fun Route.getChatsForUser(chatService: ChatService) {
    authenticate {
        get("/api/chats") {
            chatService.getChatsForUser(call.userId)
                .also {
                    call.respond(
                        HttpStatusCode.OK,
                        BasicApiResponse(
                            successful = true,
                            data = it
                        )
                    )
                }
        }
    }
}

fun Route.chatWebSocket(chatController: ChatController) {
    webSocket("/api/chat/websocket") {
        val session = call.sessions.get("SESSION") as? ChatSession
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No Session."))
            return@webSocket
        }

        try {
            incoming.consumeEach { frame ->
                when (frame) {
                    is Frame.Text -> {
                    }
                    else -> Unit
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            chatController.onDisconnect(session.userId)
        }
    }
}
