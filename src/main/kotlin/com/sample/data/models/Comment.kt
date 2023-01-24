package com.sample.data.models

import com.sample.data.responses.CommentResponse
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Comment(
    val comment: String,
    val username: String,
    val profileImageUrl: String,
    val userId: String,
    val postId: String,
    val timestamp: Long,
    val likeCount: Int,
    @BsonId
    val id: String = ObjectId().toString()
) {
    fun toCommentResponse(isLiked: Boolean): CommentResponse {
        return CommentResponse(
            id = id,
            userId = userId,
            username = username,
            profilePictureUrl = profileImageUrl,
            timestamp = timestamp,
            comment = comment,
            isLiked = isLiked,
            likeCount = likeCount
        )
    }
}
