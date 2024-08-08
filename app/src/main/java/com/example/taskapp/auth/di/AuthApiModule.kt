package com.example.taskapp.auth.di

import com.example.taskapp.auth.data.remote.api.AuthApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthApiModule {

    @Singleton
    @Provides
    fun provideAuthApiClient(retrofit: Retrofit): AuthApiClient {
        return retrofit.create(AuthApiClient::class.java)
    }

}