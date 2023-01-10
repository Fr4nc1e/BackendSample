package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeletePostRequest(
    val postId: String
)
