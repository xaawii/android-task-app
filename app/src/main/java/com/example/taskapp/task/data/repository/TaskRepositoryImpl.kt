package com.example.taskapp.task.data.repository

import com.example.taskapp.task.data.remote.api.TaskApiClient
import com.example.taskapp.task.domain.models.TaskModel
import com.example.taskapp.task.domain.repository.TaskRepository
import com.example.taskapp.task.mappers.TaskDtoMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val apiClient: TaskApiClient,
    private val taskDtoMapper: TaskDtoMapper
) : TaskRepository {
    override suspend fun getAllTasksByUserId(userId: Int): List<TaskModel> {

        return withContext(Dispatchers.IO) {
            val response = apiClient.getAllTasks(userId)
            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                taskDtoMapper.fromResponseListToDomainList(body)
            } else {
                throw Exception("Error retrieving tasks: ${response.message()}")
            }
        }

    }

    override suspend fun deleteTask(taskId: Long) {

        return withContext(Dispatchers.IO) {
            val response = apiClient.deleteTaskById(taskId)
            if (!response.isSuccessful)
                throw Exception("Error deleting task: ${response.message()}")
        }

    }

    override suspend fun deleteTaskBatch(ids: List<Long>) {

        return withContext(Dispatchers.IO) {
            val response = apiClient.deleteTasksByIdInBatch(ids)
            if (!response.isSuccessful)
                throw Exception("Error deleting tasks: ${response.message()}")
        }

    }

    override suspend fun createTask(taskModel: TaskModel, userId: Int) {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.createTask(userId, taskDtoMapper.fromDomainToRequest(taskModel))

            if (!response.isSuccessful)
                throw Exception("Error creating task: ${response.message()}")
        }
    }

    override suspend fun updateTask(taskModel: TaskModel) {
        return withContext(Dispatchers.IO) {
            val response =
                apiClient.updateTask(taskModel.id, taskDtoMapper.fromDomainToRequest(taskModel))

            if (!response.isSuccessful)
                throw Exception("Error creating task: ${response.message()}")
        }
    }
}