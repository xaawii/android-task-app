package com.example.taskapp.task.presentation.state

import com.example.taskapp.core.presentation.utils.UiText
import com.example.taskapp.task.presentation.model.TaskUIModel

sealed class DetailUIState {
    data object Loading : DetailUIState()
    data object Deleted : DetailUIState()
    data class Success(
        val task: TaskUIModel
    ) : DetailUIState()

    data class Error(val message: UiText) : DetailUIState()
}