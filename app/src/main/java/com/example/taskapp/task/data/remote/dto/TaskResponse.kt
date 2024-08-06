package com.example.taskapp.task.data.remote.dto

import com.example.taskapp.task.domain.enum.TaskStatus
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class TaskResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("createDate") val createDate: LocalDateTime,
    @SerializedName("updateDate") val updateDate: LocalDateTime,
    @SerializedName("dueDate") val dueDate: LocalDateTime,
    @SerializedName("status") val status: TaskStatus,
    @SerializedName("userId") val userId: Int
)

