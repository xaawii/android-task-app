package com.example.taskapp.task.data.remote.dto

import com.example.taskapp.task.domain.enum.TaskStatus
import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("status") val status: TaskStatus
)
