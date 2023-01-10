package com.sample.data.repository.comment

import com.sample.data.models.Comment
import com.sample.data.models.Post
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class CommentRepositoryImpl(
    db: CoroutineDatabase
): CommentRepository {
    private val comments = db.getCollection<Comment>()
    private val posts = db.getCollection<Post>()
    override suspend fun createComment(comment: Comment) {
        comments.insertOne(comment)
    }

    override suspend fun deleteComment(commentId: String): Boolean {
        return comments.deleteOneById(commentId).deletedCount > 0
    }

    override suspend fun getCommentsForPost(postId: String): List<Comment> {
        return comments.find(
            Comment::postId eq postId
        ).toList()
    }

    override suspend fun getCommentedPostForUser(userId: String): HashMap<Comment, Post> {
        val map = HashMap<Comment, Post>()
        val commentFounded = comments.find(
            Comment::userId eq userId
        ).toList().map {
            map[it] = posts.findOneById(id = it.postId)!!
        }

        return map
    }

    override suspend fun getComment(commentId: String): Comment? {
        return comments.findOneById(commentId)
    }
}
