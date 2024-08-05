package com.example.taskapp.task.domain.usecases

import com.example.taskapp.task.data.repository.TaskRepositoryImpl
import com.example.taskapp.task.domain.models.TaskModel
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val taskRepositoryImpl: TaskRepositoryImpl) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepositoryImpl.updateTask(taskModel)
    }
}