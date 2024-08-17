package com.example.taskapp.task.presentation.state

import com.example.taskapp.core.presentation.utils.UiText
import com.example.taskapp.task.domain.enum.TaskStatus
import java.time.LocalDate
import java.time.LocalTime

sealed class AddTaskUIState {
    data object Loading : AddTaskUIState()
    data class Success(val message: String) : AddTaskUIState()
    data class Error(val message: UiText) : AddTaskUIState()
    data class Editing(
        val id: Long = 0,
        val title: String = "",
        val description: String = "",
        val dueDate: LocalDate = LocalDate.now(),
        val dueTime: LocalTime = LocalTime.now(),
        val taskStatus: TaskStatus = TaskStatus.PENDING,
        val mode: String = "create",
        val formIsValid: Boolean = false
    ) : AddTaskUIState()
}