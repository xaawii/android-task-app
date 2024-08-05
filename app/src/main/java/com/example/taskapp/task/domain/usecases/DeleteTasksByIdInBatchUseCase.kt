package com.example.taskapp.task.domain.usecases

import com.example.taskapp.task.data.repository.TaskRepositoryImpl
import javax.inject.Inject

class DeleteTasksByIdInBatchUseCase @Inject constructor(private val taskRepositoryImpl: TaskRepositoryImpl) {
    suspend operator fun invoke(ids: List<Long>) {
        taskRepositoryImpl.deleteTaskBatch(ids)
    }
}