package com.sample.data.websocket

import com.sample.data.models.Message
import kotlinx.serialization.Serializable

@Serializable
data class WsServerMessage(
    val fromId: String,
    val toId: String,
    val text: String,
    val timestamp: Long,
    val chatId: String?
) {
    fun toMessage(): Message {
        return Message(
            fromId = fromId,
            toId = toId,
            text = text,
            timestamp = timestamp,
            chatId = chatId
        )
    }
}
