package com.sample.service

import com.sample.data.models.Post
import com.sample.data.repository.post.PostRepository
import com.sample.data.requests.CreatePostRequest
import com.sample.util.Constants

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

    suspend fun getPostForFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post> {
        return repository.getPostByFollows(
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }
}