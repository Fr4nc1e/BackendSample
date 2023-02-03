package com.sample.service.chat

import com.sample.data.models.Message
import com.sample.data.repository.chat.ChatRepository
import com.sample.data.responses.ChatResponse

class ChatService(
    private val chatRepository: ChatRepository
) {
    suspend fun getMessagesForChat(
        chatId: String,
        page: Int,
        pageSize: Int
    ): List<Message> {
        return chatRepository.getMessagesForChat(
            chatId = chatId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getChatsForUser(ownUserId: String): List<ChatResponse> {
        return chatRepository.getChatsForUser(ownUserId = ownUserId)
    }

    suspend fun doesChatBelongToUser(
        userId: String,
        chatId: String
    ): Boolean {
        return chatRepository.doesChatBelongToUser(
            userId = userId,
            chatId = chatId
        )
    }
}