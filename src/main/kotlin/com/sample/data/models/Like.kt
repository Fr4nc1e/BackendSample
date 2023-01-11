package com.sample.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Like(
    val userId: String,
    val parentId: String,
    val timeStamp: Long,
    val parentType: Int,
    @BsonId
    val id: String = ObjectId().toString()
)
