package com.sample.data.responses

import com.sample.data.models.Hobby
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val username: String,
    val bio: String,
    val followerCount: Int,
    val followingCount: Int,
    val postCount: Int,
    val profilePictureUrl: String,
    val topHobbyUrls: List<Hobby>,
    val gitHubUrl: String?,
    val qqUrl: String?,
    val weChatUrl: String?,
    val isOwnProfile: Boolean,
    val isFollowing: Boolean
)
