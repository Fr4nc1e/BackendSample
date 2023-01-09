package com.sample.service

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.requests.FollowUpdateRequest

class FollowService(
    private val repository: FollowRepository
) {

    suspend fun followUserIfExists(
        request: FollowUpdateRequest
    ) = repository.followUserIfExists(
        followingUserId = request.followingUserId,
        followedUserId = request.followedUserId
    )

    suspend fun unfollowUserIfExists(
        request: FollowUpdateRequest
    ) = repository.unfollowUserIfExists(
        followingUserId = request.followingUserId,
        followedUserId = request.followedUserId
    )
}
