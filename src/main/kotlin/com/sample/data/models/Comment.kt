package com.sample.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Comment(
    val comment: String,
    val userId: String,
    val postId: String,
    val timestamp: Long,
    @BsonId
    val id: String = ObjectId().toString()
)
