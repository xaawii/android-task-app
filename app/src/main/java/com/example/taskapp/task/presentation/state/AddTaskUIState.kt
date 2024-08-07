package com.example.taskapp.task.presentation.state

import com.example.taskapp.task.domain.enum.TaskStatus

sealed class AddTaskUIState {
    data object Loading : AddTaskUIState()
    data class Success(val message: String) : AddTaskUIState()
    data class Error(val message: String) : AddTaskUIState()
    data class Editing(
        val id: Long = 0,
        val title: String = "",
        val description: String = "",
        val dueDate: String = "",
        val dueTime: String = "",
        val taskStatus: TaskStatus = TaskStatus.PENDING,
        val mode: String = "create"
    ) : AddTaskUIState()
}