package com.example.taskapp.task.presentation.model

import com.example.taskapp.task.domain.enum.TaskStatus

data class TaskUIModel(
    val id: Long,
    val title: String,
    val description: String,
    val createDate: String,
    val updateDate: String,
    val dueDate: String,
    val status: TaskStatus,
    val userId: Int,
    val selected: Boolean = false
)
