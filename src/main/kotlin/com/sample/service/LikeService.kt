package com.sample.service

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.repository.like.LikeRepository
import com.sample.data.repository.user.UserRepository
import com.sample.data.responses.UserResponseItem

class LikeService(
    private val likeRepository: LikeRepository,
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun likeParent(
        userId: String,
        parentId: String,
        parentType: Int
    ): Boolean {
        return likeRepository.likeParent(
            userId = userId,
            parentId = parentId,
            parentType = parentType
        )
    }

    suspend fun unlikeParent(
        userId: String,
        parentId: String,
        parentType: Int
    ): Boolean {
        return likeRepository.unLikeParent(userId, parentId, parentType)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        likeRepository.deleteLikesForParent(parentId)
    }

    suspend fun getUsersLikedParent(
        userId: String,
        parentId: String
    ): List<UserResponseItem> {
        val userIds = likeRepository.getLikesForParent(parentId)
            .map {
                it.userId
            }

        val users = userRepository.getUsers(userIds)
        val followedByUser = followRepository.getFollowsByUser(userId)

        return users.map { user ->
            val isFollowing = followedByUser.find {
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
