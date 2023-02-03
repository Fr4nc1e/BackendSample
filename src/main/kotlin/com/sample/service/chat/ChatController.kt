package com.sample.service.chat

import com.sample.data.repository.chat.ChatRepository
import com.sample.data.websocket.WsServerMessage
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

    suspend fun sendMessage(
        frameText: String,
        message: WsServerMessage
    ) {
        onlineUsers[message.fromId]?.send(Frame.Text(frameText))
        onlineUsers[message.toId]?.send(Frame.Text(frameText))
        val messageEntity = message.toMessage()
        repository.insertMessage(messageEntity)
        if (!repository.doesChatWithUsersExist(
                userId1 = message.fromId,
                userId2 = message.toId
            )) {
            repository.insertChat(
                userId1 = message.fromId,
                userId2 = message.toId,
                messageId = messageEntity.id
            )
        } else {
            message.chatId?.let {
                repository.updateLastMessageIdForChat(
                    chatId = message.chatId,
                    lastMessageId = messageEntity.id
                )
            }
        }
    }
}