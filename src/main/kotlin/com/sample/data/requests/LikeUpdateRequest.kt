package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LikeUpdateRequest(
    val userId: String,
    val parentId: String
)
