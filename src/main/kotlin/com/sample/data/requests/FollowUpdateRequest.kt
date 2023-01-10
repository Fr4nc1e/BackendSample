package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class FollowUpdateRequest(
    val followedUserId: String
)
