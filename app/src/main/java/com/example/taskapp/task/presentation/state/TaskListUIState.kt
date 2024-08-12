package com.example.taskapp.task.presentation.state

import com.example.taskapp.core.presentation.utils.UiText
import com.example.taskapp.task.presentation.model.TaskUIModel

sealed class TaskListUIState {
    data object Loading : TaskListUIState()
    data class Success(val tasks: List<TaskUIModel>) : TaskListUIState()
    data class Error(val message: UiText) : TaskListUIState()
}