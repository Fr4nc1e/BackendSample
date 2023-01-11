package com.sample.service

import com.sample.data.models.User
import com.sample.data.repository.follow.FollowRepository
import com.sample.data.repository.user.UserRepository
import com.sample.data.requests.CreateAccountRequest
import com.sample.data.requests.UpdateProfileRequest
import com.sample.data.responses.ProfileResponse
import com.sample.data.responses.UserResponseItem

class UserService(
    private val userRepository: UserRepository,
    private val followRepository: FollowRepository
) {

    suspend fun doesUserWithEmailExist(
        email: String
    ): Boolean {
        return userRepository.getUserByEmail(email) != null
    }

    suspend fun getUserByEmail(email: String): User? {
        return userRepository.getUserByEmail(email)
    }

    suspend fun getUserProfile(
        userId: String,
        callerUserId: String
    ) : ProfileResponse? {
        val user = userRepository.getUserById(userId) ?: return null

        return ProfileResponse(
            username = user.username,
            bio = user.bio,
            followerCount = user.followerCount,
            followingCount = user.followingCount,
            postCount = user.postCount,
            profilePictureUrl = user.profileImageUrl,
            topHobbyUrls = user.hobbies,
            gitHubUrl = user.gitHubUrl,
            qqUrl = user.qqUrl,
            weChatUrl = user.weChatUrl,
            isOwnProfile = (userId == callerUserId),
            isFollowing = if (userId != callerUserId) {
                followRepository.doesUserFollow(
                    followingUserId = callerUserId,
                    followedUserId = userId
                )
            } else {
                false
            }
        )
    }

    fun isValidPassword(enteredPassword: String, actualPassword: String): Boolean {
        return enteredPassword == actualPassword
    }

    suspend fun searchUser(
        query: String,
        userId: String
    ): List<UserResponseItem> {
        val users = userRepository.searchUser(query)
        val followsByUser = followRepository.getFollowsByUser(userId)

        return users.map { user ->
            val isFollowing = followsByUser.find {
                it.followedUserId == user.id
            } != null

            UserResponseItem(
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                bio = user.bio,
                isFollowing = isFollowing
            )
        }
    }

    suspend fun updateUser(
        userId: String,
        profileImageUrl: String,
        updateProfileRequest: UpdateProfileRequest
    ) = userRepository.updateUser(
        userId,
        profileImageUrl,
        updateProfileRequest
    )

    suspend fun createUser(
        request: CreateAccountRequest
    ) {
        userRepository.createUser(
            User(
                email = request.email,
                username = request.username,
                password = request.password,
                profileImageUrl = "",
                bio = "",
                qqUrl = null,
                weChatUrl = null,
                gitHubUrl = null
            )
        )
    }

    suspend fun validateCreateAccountRequest(
        request: CreateAccountRequest
    ): ValidationEvent {
        return if (request.email.isBlank() || request.password.isBlank() || request.username.isBlank()) {
            ValidationEvent.ErrorFieldEmpty
        } else {
            ValidationEvent.Success
        }
    }

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object Success : ValidationEvent()
    }
}
