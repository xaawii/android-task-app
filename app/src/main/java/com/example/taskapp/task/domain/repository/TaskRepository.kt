package com.example.taskapp.task.domain.repository

import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.task.domain.models.TaskModel

interface TaskRepository {
    suspend fun getAllTasksByUserId(userId: Int): Result<List<TaskModel>, DataError.Network>
    suspend fun getTaskById(taskId: Long): Result<TaskModel, DataError.Network>
    suspend fun deleteTask(taskId: Long): Result<Unit, DataError.Network>
    suspend fun deleteTaskBatch(ids: List<Long>): Result<Unit, DataError.Network>
    suspend fun createTask(taskModel: TaskModel, userId: Int): Result<Unit, DataError.Network>
    suspend fun updateTask(taskModel: TaskModel): Result<Unit, DataError.Network>
}