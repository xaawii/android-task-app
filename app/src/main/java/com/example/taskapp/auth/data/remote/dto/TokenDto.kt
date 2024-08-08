package com.example.taskapp.auth.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("token") val token: String
)
