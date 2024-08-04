package com.example.taskapp.task.presentation.state

import com.example.taskapp.task.presentation.model.TaskUIModel

sealed class TaskListUIState {
    data object Loading : TaskListUIState()
    data class Success(val tasks: List<TaskUIModel>) : TaskListUIState()
    data class Error(val message: String) : TaskListUIState()
}