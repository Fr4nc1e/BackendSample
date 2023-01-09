package com.sample.di

import com.sample.data.repository.follow.FollowRepository
import com.sample.data.repository.follow.FollowRepositoryImpl
import com.sample.data.repository.user.UserRepository
import com.sample.data.repository.user.UserRepositoryImpl
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

    single<UserRepository> {
        UserRepositoryImpl(get())
    }
    single<FollowRepository> {
        FollowRepositoryImpl(get())
    }
}