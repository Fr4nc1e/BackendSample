package com.sample.data.requests.comment

import kotlinx.serialization.Serializable

@Serializable
data class DeleteCommentRequest(
    val commentId: String
)
