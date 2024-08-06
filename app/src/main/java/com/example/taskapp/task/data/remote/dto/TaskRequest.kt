package com.example.taskapp.task.data.remote.dto

import com.example.taskapp.task.domain.enum.TaskStatus
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TaskRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("dueDate") val dueDate: LocalDateTime,
    @SerializedName("status") val status: TaskStatus
)
