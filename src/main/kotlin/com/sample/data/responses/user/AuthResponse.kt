package com.sample.data.responses.user

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val userId: String,
    val token: String
)
