package com.example.taskapp.task.presentation.model

import com.example.taskapp.task.domain.enum.TaskStatus
import java.time.LocalDateTime

data class TaskUIModel(
    val id: Long,
    val title: String,
    val description: String,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime,
    val dueDate: LocalDateTime,
    val status: TaskStatus,
    val userId: Int,
    val selected: Boolean = false
)
