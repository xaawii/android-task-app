package com.example.taskapp.core.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("name") val name: String,
    @SerializedName("password") val password: String,
    @SerializedName("role") val role: String
)