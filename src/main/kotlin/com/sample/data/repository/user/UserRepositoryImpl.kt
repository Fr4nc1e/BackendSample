package com.sample.data.repository.user

import com.sample.data.models.User
import com.sample.data.requests.user.UpdateProfileRequest
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.CoroutineDatabase

class UserRepositoryImpl(
    db: CoroutineDatabase
) : UserRepository {

    private val users = db.getCollection<User>()
    override suspend fun createUser(user: User) {
        users.insertOne(user)
    }

    override suspend fun getUserById(id: String): User? {
        return users.findOneById(id)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun doesPasswordForUserMatch(
        email: String,
        enteredPassword: String
    ): Boolean {
        val user = getUserByEmail(email)
        return user?.password == enteredPassword
    }

    override suspend fun getUsers(userIds: List<String>): List<User> {
        return users.find(User::id `in` userIds).toList()
    }

    override suspend fun doesEmailBelongToUserId(
        email: String,
        userId: String
    ): Boolean {
        return users.findOneById(userId)?.email == email
    }

    override suspend fun searchUser(
        query: String,
        ownUserId: String
    ): List<User> {
        return users.find(
            and(
                or(
                    User::username regex Regex(pattern = "(?i).*$query.*"),
                    User::email eq query
                ),
                User::id ne ownUserId
            )

        )
            .descendingSort(User::followerCount)
            .toList()
    }

    override suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest
    ): Boolean {
        val user = getUserById(userId) ?: return false
        return users.updateOneById(
            id = userId,
            update = User(
                email = user.email,
                username = updateProfileRequest.username,
                password = user.password,
                profileImageUrl = profileImageUrl ?: user.profileImageUrl,
                bannerUrl = bannerUrl ?: user.bannerUrl,
                bio = updateProfileRequest.bio,
                gitHubUrl = updateProfileRequest.gitHubUrl,
                qqUrl = updateProfileRequest.qqUrl,
                weChatUrl = updateProfileRequest.weChatUrl,
                followerCount = user.followerCount,
                followingCount = user.followingCount,
                postCount = user.postCount,
                id = user.id
            )
        ).wasAcknowledged()
    }
}
