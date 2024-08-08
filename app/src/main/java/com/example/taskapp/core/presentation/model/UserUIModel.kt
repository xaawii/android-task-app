package com.example.taskapp.core.presentation.model

data class UserUIModel (
    val id: Int,
    val email: String,
    val name: String,
    val password: String,
    val role: String
)