package com.example.taskapp.task.domain.repository

import com.example.taskapp.task.domain.models.TaskModel

interface TaskRepository {
    suspend fun getAllTasksByUserId(userId: Int): List<TaskModel>
    suspend fun getTaskById(taskId: Long): TaskModel
    suspend fun deleteTask(taskId: Long)
    suspend fun deleteTaskBatch(ids: List<Long>)
    suspend fun createTask(taskModel: TaskModel, userId: Int)
    suspend fun updateTask(taskModel: TaskModel)
}