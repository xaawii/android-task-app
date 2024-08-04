package com.example.taskapp.task.domain.usecases

import com.example.taskapp.task.data.repository.TaskRepositoryImpl
import com.example.taskapp.task.domain.models.TaskModel
import javax.inject.Inject

class GetAllTasksByUserIdUseCase @Inject constructor(private val taskRepositoryImpl: TaskRepositoryImpl) {
    suspend operator fun invoke(userId: Int): List<TaskModel> {
        return taskRepositoryImpl.getAllTasksByUserId(userId)
    }
}