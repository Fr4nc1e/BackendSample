package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val description: String
)
