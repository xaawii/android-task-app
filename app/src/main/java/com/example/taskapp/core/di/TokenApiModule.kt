package com.example.taskapp.core.di

import com.example.taskapp.core.data.remote.api.TokenApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenApiModule {

    @Singleton
    @Provides
    fun provideTokenApiClient(retrofit: Retrofit): TokenApiClient {
        return retrofit.create(TokenApiClient::class.java)
    }
}