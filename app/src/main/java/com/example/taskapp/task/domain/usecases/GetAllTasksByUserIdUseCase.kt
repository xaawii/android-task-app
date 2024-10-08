package com.example.taskapp.task.domain.usecases

import com.example.taskapp.core.data.local.datastore.DataStoreManager
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.task.domain.models.TaskModel
import com.example.taskapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class GetAllTasksByUserIdUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(): Result<List<TaskModel>, DataError.Network> {
        return taskRepository.getAllTasksByUserId(dataStoreManager.getUserId()!!)
    }
}