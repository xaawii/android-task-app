package com.example.taskapp.task.domain.models

import com.example.taskapp.task.domain.enum.TaskStatus
import java.time.LocalDateTime


data class TaskModel(
    val id: Long,
    val title: String,
    val description: String,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime,
    val dueDate: LocalDateTime,
    val status: TaskStatus,
    val userId: Int
)
