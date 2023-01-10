package com.sample.data.repository.post

import com.sample.data.models.Post
import com.sample.util.Constants

interface PostRepository {

    suspend fun createPostIfUserExists(
        post: Post
    ): Boolean

    suspend fun deletePost(postId: String)

    suspend fun getPostByFollows(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post>

    suspend fun getPost(postId: String): Post?

    suspend fun getPostByLike(
        parentIdList: List<String>,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Post>
}
