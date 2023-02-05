package com.sample.data.repository.follow

import com.sample.data.models.Following
import com.sample.data.models.User

interface FollowRepository {

    suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean

    suspend fun unfollowUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean

    suspend fun getFollowsByUser(userId: String): List<Following>

    suspend fun getFollowers(userId: String): List<User>

    suspend fun getFollowings(userId: String): List<User>

    suspend fun doesUserFollow(
        followingUserId: String,
        followedUserId: String
    ): Boolean
}
