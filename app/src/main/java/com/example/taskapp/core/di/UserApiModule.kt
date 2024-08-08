package com.example.taskapp.core.di

import com.example.taskapp.core.data.remote.api.UserApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserApiModule {

    @Singleton
    @Provides
    fun provideUserApiClient(retrofit: Retrofit): UserApiClient {
        return retrofit.create(UserApiClient::class.java)
    }
}