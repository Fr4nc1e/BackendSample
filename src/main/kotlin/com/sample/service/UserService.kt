package com.sample.service

import com.sample.data.models.User
import com.sample.data.repository.user.UserRepository
import com.sample.data.requests.CreateAccountRequest

class UserService(
    private val repository: UserRepository
) {

    suspend fun doesUserWithEmailExist(
        email: String
    ): Boolean {
        return repository.getUserByEmail(email) != null
    }

    suspend fun createUser(
        request: CreateAccountRequest
    ) {
        repository.createUser(
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
