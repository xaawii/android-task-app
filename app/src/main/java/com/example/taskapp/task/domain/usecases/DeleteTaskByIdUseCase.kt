package com.example.taskapp.task.domain.usecases

import com.example.taskapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskByIdUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: Long) {
        taskRepository.deleteTask(taskId)
    }
}