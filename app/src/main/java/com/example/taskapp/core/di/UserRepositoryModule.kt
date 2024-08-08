package com.example.taskapp.core.di

import com.example.taskapp.core.data.remote.api.UserApiClient
import com.example.taskapp.core.data.repository.UserRepositoryImpl
import com.example.taskapp.core.domain.repository.UserRepository
import com.example.taskapp.core.mappers.UserDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        userApiClient: UserApiClient,
        userDtoMapper: UserDtoMapper
    ): UserRepository {
        return UserRepositoryImpl(userApiClient = userApiClient, userDtoMapper = userDtoMapper)
    }
}