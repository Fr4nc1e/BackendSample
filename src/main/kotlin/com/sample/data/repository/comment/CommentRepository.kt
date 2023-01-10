package com.sample.data.repository.comment

import com.sample.data.models.Comment
import com.sample.data.models.Post

interface CommentRepository {

    suspend fun createComment(
        comment: Comment
    )

    suspend fun deleteComment(
        commentId: String
    ) : Boolean

    suspend fun getCommentsForPost(
        postId: String
    ) : List<Comment>

    suspend fun getCommentedPostForUser(
        userId: String
    ) : HashMap<Comment, Post>

    suspend fun getComment(commentId: String): Comment?
}
