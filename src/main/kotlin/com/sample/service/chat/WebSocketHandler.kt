package com.sample.service.chat

import com.google.gson.Gson
import com.sample.data.websocket.WsClientMessage
import com.sample.util.WebSocketObject
import com.sample.util.fromJsonOrNull
import org.koin.java.KoinJavaComponent

object WebSocketHandler {
    suspend fun handleWebSocket(
        ownUserId: String,
        chatController: ChatController,
        type: Int,
        json: String
    ) {
        val gson by KoinJavaComponent.inject<Gson>(Gson::class.java)
        when (type) {
            WebSocketObject.MESSAGE.ordinal -> {
                val message = gson.fromJsonOrNull(
                    json = json,
                    clazz = WsClientMessage::class.java
                ) ?: return
                chatController.sendMessage(
                    ownUserId = ownUserId,
                    gson = gson,
                    message = message
                )
            }
        }
    }
}