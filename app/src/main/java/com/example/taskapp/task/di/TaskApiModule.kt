package com.example.taskapp.task.di

import com.example.taskapp.task.data.remote.api.TaskApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskApiModule {

    @Singleton
    @Provides
    fun provideTaskApiClient(retrofit: Retrofit): TaskApiClient {
        return retrofit.create(TaskApiClient::class.java)
    }
}