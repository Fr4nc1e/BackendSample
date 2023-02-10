package com.sample.data.responses.activity

import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    val timestamp: Long,
    val userId: String,
    val parentId: String,
    val type: Int,
    val username: String,
    val profileImageUrl: String,
    val id: String
)
