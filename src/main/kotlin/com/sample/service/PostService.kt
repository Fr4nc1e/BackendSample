package com.sample.service

import com.sample.data.models.Post
import com.sample.data.repository.post.PostRepository
import com.sample.data.requests.CreatePostRequest
import com.sample.data.responses.PostResponse
import com.sample.util.Constants

class PostService(
    private val postRepository: PostRepository
) {
    suspend fun createPost(
        request: CreatePostRequest,
        userId: String,
        contentUrl: String
    ) = postRepository.createPost(
        Post(
            contentUrl = contentUrl,
            userId = userId,
            timestamp = System.currentTimeMillis(),
            description = request.description
        )
    )

    suspend fun getPostForFollows(
        ownUserId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse> {
        return postRepository.getPostByFollows(
            ownUserId = ownUserId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getPost(postId: String): Post? {
        return postRepository.getPost(postId)
    }

    suspend fun getPostDetails(userId: String, postId: String): PostResponse? {
        return postRepository.getPostDetails(userId, postId)
    }

    suspend fun getPostForLike(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<PostResponse> {
        return postRepository.getPostForLike(
            ownUserId = ownUserId,
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun deletePost(postId: String) {
        postRepository.deletePost(postId)
    }

    suspend fun getPostForProfile(
        ownUserId: String,
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ) : List<PostResponse> {
        return postRepository.getPostForProfile(
            ownUserId = ownUserId,
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }
}
