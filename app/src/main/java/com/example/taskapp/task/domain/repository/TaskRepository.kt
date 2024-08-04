package com.example.taskapp.task.domain.repository

import com.example.taskapp.task.domain.models.TaskModel

interface TaskRepository {
    suspend fun getAllTasksByUserId(userId: Int): List<TaskModel>
    suspend fun deleteTask(taskId: Long)
}