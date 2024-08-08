package com.example.taskapp.core.di

import com.example.taskapp.core.data.remote.api.TokenApiClient
import com.example.taskapp.core.data.repository.TokenRepositoryImpl
import com.example.taskapp.core.domain.repository.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TokenRepositoryModule {

    @Singleton
    @Provides
    fun provideTokenRepository(
        tokenApiClient: TokenApiClient
    ): TokenRepository {
        return TokenRepositoryImpl(tokenApiClient)
    }
}