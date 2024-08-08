package com.example.taskapp.core.domain.model


data class UserModel(
    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val role: String
)