package com.example.taskapp.task.presentation.state

import com.example.taskapp.core.presentation.utils.UiText
import com.example.taskapp.task.presentation.model.TaskUIModel
import java.time.LocalDate
import java.time.YearMonth

sealed class TaskListUIState {
    data object Loading : TaskListUIState()
    data class Success(
        val tasks: List<TaskUIModel>,
        val selectedDate: LocalDate = LocalDate.now(),
        val yearMonth: YearMonth = YearMonth.now()
    ) : TaskListUIState()

    data class Error(val message: UiText) : TaskListUIState()
    data object LogOut : TaskListUIState()
}