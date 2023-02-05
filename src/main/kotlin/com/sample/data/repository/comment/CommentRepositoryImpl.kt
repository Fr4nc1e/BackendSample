package com.sample.data.repository.comment

import com.sample.data.models.Comment
import com.sample.data.models.Like
import com.sample.data.models.Post
import com.sample.data.models.User
import com.sample.data.responses.CommentResponse
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class CommentRepositoryImpl(
    db: CoroutineDatabase
): CommentRepository {
    private val comments = db.getCollection<Comment>()
    private val posts = db.getCollection<Post>()
    private val likes = db.getCollection<Like>()
    private val users = db.getCollection<User>()

    override suspend fun createComment(comment: Comment) : String {
        comments.insertOne(comment)
        val oldCommentCount = posts.findOneById(comment.postId)?.commentCount ?: 0
        posts.updateOneById(
            id = comment.postId,
            update = setValue(Post::commentCount, oldCommentCount + 1)
        )
        return comment.id
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        return comments.deleteOneById(commentId).deletedCount > 0
    }

    override suspend fun deleteCommentsFromPost(postId: String): Boolean {
        return comments.deleteMany(
            Comment::postId eq postId
        )
            .wasAcknowledged()
    }

    override suspend fun getCommentsForPost(
        postId: String,
        ownUserId: String
    ): List<CommentResponse> {
        return comments.find(Comment::postId eq postId)
            .descendingSort(Comment::timestamp)
            .toList().map { comment ->
            val isLiked = likes.findOne(
                and(
                    Like::userId eq ownUserId,
                    Like::parentId eq comment.id
                )
            ) != null
            comment.toCommentResponse(isLiked)
        }
    }

    override suspend fun getCommentsForUser(
        ownUserId: String,
        userId: String,
        page: Int,
        pageSize: Int
    ): List<CommentResponse> {
        users.findOneById(userId) ?: return emptyList()
        return comments.find(Comment::userId eq userId)
            .skip(page * pageSize)
            .limit(pageSize)
            .descendingSort(Comment::timestamp)
            .toList().map { comment ->
                val isLiked = likes.findOne(
                    and(
                        Like::userId eq ownUserId,
                        Like::parentId eq comment.id
                    )
                ) != null
                comment.toCommentResponse(isLiked)
            }
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }
}
