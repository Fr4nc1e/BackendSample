package com.sample.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val email: String,
    val username: String,
    val password: String,
    val profileImageUrl: String,
    val bannerUrl: String,
    val bio: String,
    val gitHubUrl: String?,
    val qqUrl: String?,
    val weChatUrl: String?,
    val followerCount: Int = 0,
    val followingCount: Int = 0,
    val postCount: Int = 0,
    @BsonId
    val id: String = ObjectId().toString()
)
