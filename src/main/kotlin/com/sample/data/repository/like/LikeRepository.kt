package com.sample.data.repository.like

import com.sample.data.models.Like
import com.sample.util.Constants

interface LikeRepository {

    suspend fun likeParent(
        userId: String,
        parentId: String,
        parentType: Int
    ): Boolean

    suspend fun unLikeParent(
        userId: String,
        parentId: String,
        parentType: Int
    ): Boolean

    suspend fun deleteLikesForParent(parentId: String)

    suspend fun getLikesForParent(
        postId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ) : List<Like>
}
