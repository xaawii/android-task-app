package com.example.taskapp.task.domain.usecases

import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTasksByIdInBatchUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(ids: List<Long>): Result<Unit, DataError.Network> {
        return taskRepository.deleteTaskBatch(ids)
    }
}