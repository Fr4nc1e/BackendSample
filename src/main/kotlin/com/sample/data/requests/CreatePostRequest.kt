package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val userId: String,
    val description: String,
    val imageUrl: String?
)
