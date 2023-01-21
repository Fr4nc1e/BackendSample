package com.sample.data.repository.follow

import com.sample.data.models.Following
import com.sample.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.`in`

class FollowRepositoryImpl(
    db: CoroutineDatabase
) : FollowRepository {

    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()

    override suspend fun followUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        val doesFollowingUserExist = users.findOneById(followingUserId) != null
        val doesFollowedUserExist = users.findOneById(followedUserId) != null

        if (!doesFollowedUserExist || !doesFollowingUserExist) return false

        following.insertOne(
            Following(
                followingUserId = followingUserId,
                followedUserId = followedUserId
            )
        )

        return true
    }

    override suspend fun unfollowUserIfExists(
        followingUserId: String,
        followedUserId: String
    ): Boolean {
        val deleteResult = following.deleteOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId
            )
        )

        return deleteResult.deletedCount > 0
    }

    override suspend fun getFollowsByUser(userId: String): List<Following> {
        return following.find(
            Following::followingUserId eq userId
        ).toList()
    }

    override suspend fun getFollowedUsers(userId: String): List<User> {
        val followList = following.find(
            Following::followingUserId eq userId
        ).toList()
            .map { it.followedUserId }
        return users.find(
            User::id `in` followList
        ).toList()
    }

    override suspend fun getFollowingUsers(userId: String): List<User> {
        val followList = following.find(
            Following::followedUserId eq userId
        ).toList()
            .map { it.followingUserId }
        return users.find(
            User::id `in` followList
        ).toList()
    }

    override suspend fun doesUserFollow(followingUserId: String, followedUserId: String): Boolean {
        return following.findOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId
            )
        ) != null
    }
}
