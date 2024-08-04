package com.example.taskapp.task.domain.models

import com.example.taskapp.task.domain.enum.TaskStatus


data class TaskModel(
    val id: Long,
    val title: String,
    val description: String,
    val createDate: String,
    val updateDate: String,
    val dueDate: String,
    val status: TaskStatus,
    val userId: Int
)
