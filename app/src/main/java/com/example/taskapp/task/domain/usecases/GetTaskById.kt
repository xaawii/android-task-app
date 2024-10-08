package com.example.taskapp.task.domain.usecases

import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.task.domain.models.TaskModel
import com.example.taskapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskById @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: Long): Result<TaskModel, DataError.Network> {
        return taskRepository.getTaskById(taskId)
    }
}