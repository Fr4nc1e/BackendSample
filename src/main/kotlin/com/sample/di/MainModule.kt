package com.sample.di

import com.google.gson.Gson
import com.sample.data.repository.activity.ActivityRepository
import com.sample.data.repository.activity.ActivityRepositoryImpl
import com.sample.data.repository.chat.ChatRepository
import com.sample.data.repository.chat.ChatRepositoryImpl
import com.sample.data.repository.comment.CommentRepository
import com.sample.data.repository.comment.CommentRepositoryImpl
import com.sample.data.repository.follow.FollowRepository
import com.sample.data.repository.follow.FollowRepositoryImpl
import com.sample.data.repository.like.LikeRepository
import com.sample.data.repository.like.LikeRepositoryImpl
import com.sample.data.repository.post.PostRepository
import com.sample.data.repository.post.PostRepositoryImpl
import com.sample.data.repository.user.UserRepository
import com.sample.data.repository.user.UserRepositoryImpl
import com.sample.service.*
import com.sample.service.chat.ChatService
import com.sample.util.Constants
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        val client = KMongo.createClient(
            Constants.DATABASE_URL
        ).coroutine
        client.getDatabase(Constants.DATABASE_NAME)
    }

    // User
    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single { UserService(get(), get()) }

    // Follow
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
    single { FollowService(get()) }

    // Post
    single<PostRepository> {
        PostRepositoryImpl(get())
    }
    single { PostService(get()) }

    // Like
    single<LikeRepository> {
        LikeRepositoryImpl(get())
    }
    single { LikeService(get(), get(), get()) }
    
    // Comment
    single<CommentRepository> { 
        CommentRepositoryImpl(get())
    }
    single { CommentService(get(), get()) }

    // Activity
    single<ActivityRepository> {
        ActivityRepositoryImpl(get())
    }
    single { ActivityService(get(), get(), get()) }

    // Chat
    single<ChatRepository> {
        ChatRepositoryImpl(get())
    }
    single { ChatService(get()) }
    single { ChatService(get()) }

    // Gson
    single { Gson() }
}
