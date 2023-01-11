package com.sample.data.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Hobby(
    @BsonId
    val id: String = ObjectId().toString(),
    val hobby: String,
    val iconUrl: String
)
