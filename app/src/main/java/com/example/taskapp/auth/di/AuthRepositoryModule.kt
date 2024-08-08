package com.example.taskapp.auth.di

import com.example.taskapp.auth.data.remote.api.AuthApiClient
import com.example.taskapp.auth.data.repository.AuthRepositoryImpl
import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.auth.mappers.AuthDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiClient: AuthApiClient,
        authDtoMapper: AuthDtoMapper
    ): AuthRepository {
        return AuthRepositoryImpl(authApiClient = authApiClient, authDtoMapper = authDtoMapper)
    }

}