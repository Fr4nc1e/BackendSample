package com.sample.data.responses.post

import kotlinx.serialization.Serializable

@Serializable
data class PostResponse(
    val id: String,
    val userId: String,
    val username: String,
    val contentUrl: String,
    val profilePictureUrl: String,
    val timestamp: Long,
    val description: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val isOwnPost: Boolean
)
