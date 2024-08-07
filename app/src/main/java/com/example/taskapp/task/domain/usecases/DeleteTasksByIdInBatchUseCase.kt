package com.example.taskapp.task.domain.usecases

import com.example.taskapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTasksByIdInBatchUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(ids: List<Long>) {
        taskRepository.deleteTaskBatch(ids)
    }
}