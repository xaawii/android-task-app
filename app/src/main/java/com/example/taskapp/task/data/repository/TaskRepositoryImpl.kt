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
) :
    TaskRepository {
    override suspend fun getAllTasksByUserId(userId: Int): List<TaskModel> {

        return withContext(Dispatchers.IO) {
            val response = apiClient.getAllTasks(userId)
            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                taskDtoMapper.fromResponseListToDomainList(body)
            } else {
                throw Exception("Error al obtener tareas: ${response.message()}")
            }
        }

    }

    override suspend fun deleteTask(taskId: Long) {

        return withContext(Dispatchers.IO) {
            val response = apiClient.deleteTaskById(taskId)
            if (!response.isSuccessful)
                throw Exception("Error al obtener tareas: ${response.message()}")
        }

    }
}