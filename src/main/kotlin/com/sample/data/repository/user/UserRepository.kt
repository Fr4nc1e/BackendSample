package com.sample.data.repository.user

import com.sample.data.models.User
import com.sample.data.requests.user.UpdateProfileRequest

interface UserRepository {

    suspend fun createUser(user: User)

    suspend fun getUserById(id: String) : User?

    suspend fun getUserByEmail(email: String) : User?

    suspend fun getUsers(userIds: List<String>): List<User>

    suspend fun doesPasswordForUserMatch(
        email: String,
        enteredPassword: String
    ): Boolean

    suspend fun doesEmailBelongToUserId(
        email: String,
        userId: String
    ) : Boolean

    suspend fun searchUser(
        query: String,
        ownUserId: String
    ) : List<User>

    suspend fun updateUser(
        userId: String,
        profileImageUrl: String?,
        bannerUrl: String?,
        updateProfileRequest: UpdateProfileRequest
    ) : Boolean
}
