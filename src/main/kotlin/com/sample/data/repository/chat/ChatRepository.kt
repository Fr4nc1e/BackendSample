package com.sample.data.repository.chat

import com.sample.data.models.Chat
import com.sample.data.models.Message

interface ChatRepository {
    suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): List<Message>

    suspend fun getChatsForUser(
        ownUserId: String
    ): List<Chat>

    suspend fun doesChatBelongToUser(
        chatId: String,
        userId: String
    ): Boolean

    suspend fun insertMessage(message: Message)
}