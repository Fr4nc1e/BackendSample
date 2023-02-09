package com.sample.data.repository.chat

import com.sample.data.models.Message
import com.sample.data.responses.ChatResponse

interface ChatRepository {
    suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): List<Message>

    suspend fun getChatsForUser(
        ownUserId: String
    ): List<ChatResponse>

    suspend fun doesChatBelongToUser(
        chatId: String,
        userId: String
    ): Boolean

    suspend fun insertMessage(message: Message)

    suspend fun insertChat(
        userId1: String,
        userId2: String,
        messageId: String
    ): String

    suspend fun doesChatWithUsersExist(
        userId1: String,
        userId2: String
    ): Boolean

    suspend fun returnUsersChatChannel(
        userId1: String,
        userId2: String
    ): String?

    suspend fun updateLastMessageIdForChat(
        chatId: String,
        lastMessageId: String
    )
}