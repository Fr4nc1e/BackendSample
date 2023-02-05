package com.sample.data.repository.comment

import com.sample.data.models.Comment
import com.sample.data.responses.CommentResponse

interface CommentRepository {

    suspend fun createComment(comment: Comment) : String

    suspend fun deleteComment(commentId: String) : Boolean

    suspend fun deleteCommentsFromPost(postId: String): Boolean

    suspend fun getCommentsForPost(
        postId: String,
        ownUserId: String
    ) : List<CommentResponse>

    suspend fun getCommentsForUser(
        ownUserId: String,
        userId: String,
        page: Int,
        pageSize: Int
    ) : List<CommentResponse>

    suspend fun getComment(commentId: String): Comment?
}
