package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class LikeUpdateRequest(
    val parentId: String,
    val parentType: Int
)
