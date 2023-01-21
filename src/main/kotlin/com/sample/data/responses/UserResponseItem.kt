package com.sample.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseItem(
    val userId: String,
    val username: String,
    val profileImageUrl: String,
    val bio: String,
    val isFollowing: Boolean
)
