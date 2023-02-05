package com.sample.service

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.requests.FollowUpdateRequest
import com.sample.data.responses.UserResponseItem

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
    suspend fun getFollowings(userId: String): List<UserResponseItem> {
        return followRepository.getFollowings(userId).map { user ->
            val isFollowing = followRepository.getFollowsByUser(userId)
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

    suspend fun getFollowers(userId: String): List<UserResponseItem> {
        return followRepository.getFollowers(userId).map { user ->
            val isFollowing = followRepository.getFollowsByUser(userId)
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
