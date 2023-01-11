package com.sample.data.repository.comment

import com.sample.data.models.Comment
import com.sample.data.models.Post
import com.sample.util.Constants

interface CommentRepository {

    suspend fun createComment(comment: Comment) : String

    suspend fun deleteComment(commentId: String) : Boolean

    suspend fun deleteCommentsFromPost(postId: String): Boolean

    suspend fun getCommentsForPost(
        postId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ) : List<Comment>

    suspend fun getCommentedPostForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ) : HashMap<Comment, Post>

    suspend fun getComment(commentId: String): Comment?
}
