package com.example.taskapp.task.data.remote.dto

import com.example.taskapp.task.domain.enum.TaskStatus
import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("createDate") val createDate: String,
    @SerializedName("updateDate") val updateDate: String,
    @SerializedName("dueDate") val dueDate: String,
    @SerializedName("status") val status: TaskStatus,
    @SerializedName("userId") val userId: Int
)

