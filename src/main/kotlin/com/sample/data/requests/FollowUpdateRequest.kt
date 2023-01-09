package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class FollowUpdateRequest(
    val followingUserId: String,
    val followedUserId: String
)
