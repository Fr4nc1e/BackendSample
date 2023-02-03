package com.sample.routes

import com.google.gson.Gson
import com.sample.data.responses.BasicApiResponse
import com.sample.data.websocket.WsServerMessage
import com.sample.routes.util.userId
import com.sample.service.chat.ChatController
import com.sample.service.chat.ChatService
import com.sample.service.chat.ChatSession
import com.sample.util.Constants
import com.sample.util.QueryParams
import com.sample.util.WebSocketObject
import com.sample.util.fromJsonOrNull
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.java.KoinJavaComponent.inject

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
        val session = call.sessions.get<ChatSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No Session."))
            return@webSocket
        }
        chatController.onJoin(session, this)
        try {
            incoming.consumeEach { frame ->
                kotlin.run {
                    when (frame) {
                        is Frame.Text -> {
                            val frameText = frame.readText()
                            val delimiterIndex = frameText.indexOf("#")
                            if (delimiterIndex == -1) {
                                println("No delimiter index.")
                                return@run
                            }
                            val type = frameText.substring(
                                startIndex = 0,
                                endIndex = delimiterIndex
                            ).toIntOrNull()
                            if (type == null) {
                                println("Invalid format")
                                return@run
                            }
                            val json = frameText.substring(
                                startIndex = delimiterIndex + 1,
                                endIndex = frameText.length
                            )
                            handleWebSocket(
                                webSocketSession = this,
                                session = session,
                                chatController = chatController,
                                type = type,
                                frameText = frameText,
                                json = json
                            )
                        }
                        else -> Unit
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            chatController.onDisconnect(session.userId)
        }
    }
}

suspend fun handleWebSocket(
    webSocketSession: WebSocketSession,
    session: ChatSession,
    chatController: ChatController,
    type: Int,
    frameText: String,
    json: String
) {
    val gson by inject<Gson>(Gson::class.java)
    when(type) {
        WebSocketObject.MESSAGE.ordinal -> {
            val message = gson.fromJsonOrNull(
                json = json,
                clazz = WsServerMessage::class.java
            ) ?: return
            chatController.sendMessage(
                frameText = frameText,
                message = message
            )
        }
    }
}
