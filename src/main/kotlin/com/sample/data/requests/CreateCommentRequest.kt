package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class CreateCommentRequest(
    val comment: String,
    val postId: String
)
