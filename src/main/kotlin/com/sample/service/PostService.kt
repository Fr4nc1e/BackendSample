package com.sample.service

import com.sample.data.models.Post
import com.sample.data.repository.post.PostRepository
import com.sample.data.requests.CreatePostRequest

class PostService(
    private val repository: PostRepository
) {
    suspend fun createPostIfUserExists(
        request: CreatePostRequest
    ) = repository.createPostIfUserExists(
        Post(
            imageUrl = request.imageUrl,
            userId = request.userId,
            timestamp = System.currentTimeMillis(),
            description = request.description
        )
    )
}