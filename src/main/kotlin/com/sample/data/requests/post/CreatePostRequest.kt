package com.sample.data.requests.post

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val description: String
)
