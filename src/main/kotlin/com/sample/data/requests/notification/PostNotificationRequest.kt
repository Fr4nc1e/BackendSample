package com.sample.data.requests.notification

import com.sample.data.models.NotificationMessage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostNotificationRequest(
    @SerialName("include_external_user_ids")
    val includeExternalUserIds: List<String>,
    val contents: NotificationMessage,
    val headings: NotificationMessage,
    @SerialName("app_id")
    val appId: String,
)
