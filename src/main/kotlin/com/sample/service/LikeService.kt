package com.sample.service

import com.sample.data.repository.like.LikeRepository
import com.sample.util.Constants

class LikeService(
    private val repository: LikeRepository
) {

    suspend fun likeParent(
        userId: String,
        parentId: String,
        parentType: Int
    ): Boolean {
        return repository.likeParent(
            userId = userId,
            parentId = parentId,
            parentType = parentType
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
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<String> {
        return repository.getLikedEntitiesForUser(
            userId,
            page,
            pageSize
        )
    }
}
