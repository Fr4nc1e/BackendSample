package com.sample.service

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.requests.follow.FollowUpdateRequest
import com.sample.data.responses.user.UserResponseItem

class FollowService(
    private val followRepository: FollowRepository,
) {
    suspend fun followUserIfExists(
        request: FollowUpdateRequest,
        followingUserId: String
    ) = followRepository.followUserIfExists(
        followingUserId = followingUserId,
        followedUserId = request.followedUserId
    )
    suspend fun unfollowUserIfExists(
        request: FollowUpdateRequest,
        followingUserId: String
    ) = followRepository.unfollowUserIfExists(
        followingUserId = followingUserId,
        followedUserId = request.followedUserId
    )
    suspend fun getFollowings(
        ownUserId: String,
        userId: String
    ): List<UserResponseItem> {
        return followRepository.getFollowings(userId).map { user ->
            val isFollowing = followRepository.getFollowsByUser(ownUserId)
                .find {
                    it.followedUserId == user.id
                } != null

            UserResponseItem(
                userId = user.id,
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }

    suspend fun getFollowers(
        ownUserId: String,
        userId: String
    ): List<UserResponseItem> {
        return followRepository.getFollowers(userId).map { user ->
            val isFollowing = followRepository.getFollowsByUser(ownUserId)
                .find {
                    it.followedUserId == user.id
                } != null

            UserResponseItem(
                userId = user.id,
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }
}
