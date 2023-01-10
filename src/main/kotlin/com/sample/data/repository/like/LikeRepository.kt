package com.sample.data.repository.like

interface LikeRepository {

    suspend fun likeParent(
        userId: String,
        parentId: String
    ): Boolean

    suspend fun unLikeParent(
        userId: String,
        parentId: String
    ): Boolean

    suspend fun deleteLikesForParent(parentId: String)

    suspend fun getLikedEntitiesForUser(
        userId: String
    ): List<String>
}
