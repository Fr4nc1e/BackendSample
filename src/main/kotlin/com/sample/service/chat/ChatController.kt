package com.sample.service.chat

import com.sample.data.models.Message
import com.sample.data.repository.chat.ChatRepository
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap

class ChatController(
    private val repository: ChatRepository
) {
    private val onlineUsers = ConcurrentHashMap<String, WebSocketSession>()

    fun onJoin(
        chatSession: ChatSession,
        socket: WebSocketSession
    ) {
        onlineUsers[chatSession.userId] = socket
    }

    fun onDisconnect(userId: String) {
        if (onlineUsers.containsKey(userId)) {
            onlineUsers.remove(userId)
        }
    }

    suspend fun sendMessage(message: Message) {
        onlineUsers[message.fromId]?.send(Frame.Text(message.text))
        repository.insertMessage(message)
    }
}