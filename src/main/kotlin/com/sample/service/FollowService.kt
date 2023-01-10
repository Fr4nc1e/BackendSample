package com.sample.service

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.requests.FollowUpdateRequest

class FollowService(
    private val repository: FollowRepository
) {

    suspend fun followUserIfExists(
        request: FollowUpdateRequest,
        followingUserId: String
    ) = repository.followUserIfExists(
        followingUserId = followingUserId,
        followedUserId = request.followedUserId
    )

    suspend fun unfollowUserIfExists(
        request: FollowUpdateRequest,
        followingUserId: String
    ) = repository.unfollowUserIfExists(
        followingUserId = followingUserId,
        followedUserId = request.followedUserId
    )
}
