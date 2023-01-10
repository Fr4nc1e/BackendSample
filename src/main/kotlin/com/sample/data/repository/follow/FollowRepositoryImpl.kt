package com.sample.data.repository.follow

import com.sample.data.models.Following
import com.sample.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

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
}
