package com.sample.data.requests

import com.sample.data.models.Hobby
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String,
    val bio: String,
    val qqUrl: String,
    val weChatUrl: String,
    val gitHubUrl: String,
    val hobbies: List<Hobby>
)
