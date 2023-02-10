package com.sample.data.models

import com.sample.data.responses.post.PostResponse
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Post(
    val contentUrl: String,
    val userId: String,
    val timestamp: Long,
    val description: String,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    @BsonId
    val id: String = ObjectId().toString()
) {
    fun toPostResponse(
        username: String,
        profilePictureUrl: String,
        isLiked: Boolean,
        isOwnPost: Boolean
    ): PostResponse {
        return PostResponse(
            id = id,
            userId = userId,
            username = username,
            contentUrl = contentUrl,
            profilePictureUrl = profilePictureUrl,
            timestamp = timestamp,
            description = description,
            likeCount = likeCount,
            commentCount = commentCount,
            isLiked = isLiked,
            isOwnPost = isOwnPost
        )
    }
}
