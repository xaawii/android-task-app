package com.example.taskapp.task.domain.usecases

import com.example.taskapp.core.data.local.datastore.DataStoreManager
import com.example.taskapp.task.domain.models.TaskModel
import com.example.taskapp.task.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dataStoreManager: DataStoreManager
) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.createTask(taskModel, dataStoreManager.getUserId()!!)
    }
}