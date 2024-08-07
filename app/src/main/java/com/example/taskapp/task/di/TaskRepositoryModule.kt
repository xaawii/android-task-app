package com.example.taskapp.task.di

import com.example.taskapp.task.data.remote.api.TaskApiClient
import com.example.taskapp.task.data.repository.TaskRepositoryImpl
import com.example.taskapp.task.domain.repository.TaskRepository
import com.example.taskapp.task.mappers.TaskDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskRepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        apiClient: TaskApiClient,
        taskDtoMapper: TaskDtoMapper
    ): TaskRepository {
        return TaskRepositoryImpl(apiClient = apiClient, taskDtoMapper = taskDtoMapper)
    }

}