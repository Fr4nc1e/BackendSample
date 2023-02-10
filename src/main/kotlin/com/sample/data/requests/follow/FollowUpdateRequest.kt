package com.sample.data.requests.follow

import kotlinx.serialization.Serializable

@Serializable
data class FollowUpdateRequest(
    val followedUserId: String
)
