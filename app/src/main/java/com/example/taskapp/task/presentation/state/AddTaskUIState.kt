package com.example.taskapp.task.presentation.state

import com.example.taskapp.task.presentation.model.TaskUIModel

sealed class AddTaskUIState {
    data object Loading : AddTaskUIState()
    data class Success(val task: TaskUIModel) : AddTaskUIState()
    data class Error(val message: String) : AddTaskUIState()
}