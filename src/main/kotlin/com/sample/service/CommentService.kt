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
        request: CreateCommentRequest
    ): ValidationEvent {
        request.apply {
            if (comment.isBlank() || userId.isBlank() || postId.isBlank()) {
                return ValidationEvent.ErrorFieldEmpty
            }
            if (comment.length > Constants.MAX_COMMENT_LENGTH) {
                return ValidationEvent.ErrorCommentTooLong
            }
        }

        repository.createComment(
            Comment(
                comment = request.comment,
                userId = request.userId,
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

    suspend fun getCommentsForPost(postId: String): List<Comment> {
        return repository.getCommentsForPost(postId)
    }

    suspend fun getCommentedPostForUser(userId: String): HashMap<Comment, Post> {
        return repository.getCommentedPostForUser(userId)
    }

    suspend fun getComment(commentId: String) =
        repository.getComment(commentId)

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object Success : ValidationEvent()
    }
}
