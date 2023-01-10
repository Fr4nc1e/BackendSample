package com.sample.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteCommentRequest(
    val commentId: String
)
