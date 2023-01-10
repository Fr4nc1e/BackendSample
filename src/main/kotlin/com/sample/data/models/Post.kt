package com.sample.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Post(
    val imageUrl: String?,
    val userId: String,
    val timestamp: Long,
    val description: String,
    @BsonId
    val id: String = ObjectId().toString()
)