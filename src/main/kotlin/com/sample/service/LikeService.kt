package com.sample.service

import com.sample.data.repository.like.LikeRepository

class LikeService(
    private val repository: LikeRepository
) {

    suspend fun likeParent(
        userId: String,
        parentId: String
    ): Boolean {
        return repository.likeParent(
            userId = userId,
            parentId = parentId
        )
    }

    suspend fun unlikeParent(
        userId: String,
        parentId: String
    ): Boolean {
        return repository.unLikeParent(userId, parentId)
    }

    suspend fun deleteLikesForParent(parentId: String) {
        repository.deleteLikesForParent(parentId)
    }

    suspend fun getLikeEntityForUser(
        userId: String
    ): List<String> {
        return repository.getLikedEntitiesForUser(userId)
    }
}
