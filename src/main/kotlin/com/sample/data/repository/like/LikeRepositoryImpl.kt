package com.sample.data.repository.like

import com.sample.data.models.Like
import com.sample.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class LikeRepositoryImpl(
    db: CoroutineDatabase
) : LikeRepository {

    private val likes = db.getCollection<Like>()
    private val users = db.getCollection<User>()

    override suspend fun likeParent(
        userId: String,
        parentId: String,
        parentType: Int
    ): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist) {
            likes.insertOne(
                Like(
                    userId = userId,
                    parentId = parentId,
                    timeStamp = System.currentTimeMillis(),
                    parentType = parentType
                )
            )
            true
        } else false
    }

    override suspend fun unLikeParent(userId: String, parentId: String): Boolean {
        val doesUserExist = users.findOneById(userId) != null
        return if (doesUserExist) {
            likes.deleteOne(
                and(
                    Like::userId eq userId,
                    Like::parentId eq parentId
                )
            )
            true
        } else false
    }

    override suspend fun deleteLikesForParent(parentId: String) {
        likes.deleteMany(Like::parentId eq parentId)
    }

    override suspend fun getLikedEntitiesForUser(
        userId: String,
        page: Int,
        pageSize: Int
    ): List<String> {
        return likes.find(
            Like::userId eq userId
        )
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Like::timeStamp)
            .toList()
            .map {
                it.parentId
            }
    }
}
