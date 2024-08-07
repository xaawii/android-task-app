package com.example.taskapp.task.domain.usecases

import com.example.taskapp.task.domain.models.TaskModel
import com.example.taskapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class GetAllTasksByUserIdUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(userId: Int): List<TaskModel> {
        return taskRepository.getAllTasksByUserId(userId)
    }
}