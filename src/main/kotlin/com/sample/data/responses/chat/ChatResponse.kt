package com.sample.data.responses.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatResponse(
    val chatId: String,
    val remoteUserId: String?,
    val remoteUsername: String?,
    val remoteUserProfilePictureUrl: String?,
    val lastMessage: String?,
    val timestamp: Long?
)
