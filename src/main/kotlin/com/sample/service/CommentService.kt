package com.sample.service

import com.sample.data.models.Comment
import com.sample.data.models.Post
import com.sample.data.repository.comment.CommentRepository
import com.sample.data.requests.CreateCommentRequest
import com.sample.util.Constants

class CommentService(
    private val repository: CommentRepository
) {

    suspend fun createComment(
        request: CreateCommentRequest,
        userId: String
    ): ValidationEvent {
        request.apply {
            if (comment.isBlank() || postId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if (comment.length > Constants.MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }

        repository.createComment(
            Comment(
                comment = request.comment,
                userId = userId,
                postId = request.postId,
                timestamp = System.currentTimeMillis()
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(
        commentId: String
    ) : Boolean {
        return repository.deleteComment(commentId)
    }

    suspend fun deleteCommentForPost(postId: String) {
        repository.deleteCommentsFromPost(postId)
    }

    suspend fun getCommentsForPost(
        postId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): List<Comment> {
        return repository.getCommentsForPost(
            postId,
            page,
            pageSize
        )
    }

    suspend fun getCommentedPostForUser(
        userId: String,
        page: Int = 0,
        pageSize: Int = Constants.DEFAULT_POST_PAGE_SIZE
    ): HashMap<Comment, Post> {
        return repository.getCommentedPostForUser(
            userId,
            page,
            pageSize
        )
    }

    suspend fun getCommentById(commentId: String) =
        repository.getComment(commentId)

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object Success : ValidationEvent()
    }
}
