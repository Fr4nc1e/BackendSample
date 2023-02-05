package com.sample.service

import com.sample.data.models.Comment
import com.sample.data.repository.comment.CommentRepository
import com.sample.data.repository.user.UserRepository
import com.sample.data.requests.CreateCommentRequest
import com.sample.data.responses.CommentResponse
import com.sample.util.Constants

class CommentService(
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository
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

        val user = userRepository.getUserById(userId) ?: return ValidationEvent.UserNotFound
        commentRepository.createComment(
            Comment(
                username = user.username,
                profileImageUrl = user.profileImageUrl,
                likeCount = 0,
                comment = request.comment,
                userId = userId,
                postId = request.postId,
                timestamp = System.currentTimeMillis(),
            )
        )
        return ValidationEvent.Success
    }

    suspend fun deleteComment(
        commentId: String
    ) : Boolean {
        return commentRepository.deleteComment(commentId)
    }

    suspend fun deleteCommentForPost(postId: String) {
        commentRepository.deleteCommentsFromPost(postId)
    }

    suspend fun getCommentsForPost(
        postId: String,
        ownUserId: String
    ): List<CommentResponse> {
        return commentRepository.getCommentsForPost(
            postId,
            ownUserId
        )
    }

    suspend fun getCommentsForUser(
        ownUserId: String,
        userId: String,
        page: Int,
        pageSize: Int
    ): List<CommentResponse> {
        return commentRepository.getCommentsForUser(
            ownUserId = ownUserId,
            userId = userId,
            page = page,
            pageSize = pageSize
        )
    }

    suspend fun getCommentById(commentId: String) =
        commentRepository.getComment(commentId)

    sealed class ValidationEvent {
        object ErrorFieldEmpty : ValidationEvent()
        object ErrorCommentTooLong : ValidationEvent()
        object Success : ValidationEvent()
        object UserNotFound : ValidationEvent()
    }
}
