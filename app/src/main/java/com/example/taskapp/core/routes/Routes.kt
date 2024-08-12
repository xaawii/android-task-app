package com.example.taskapp.core.routes

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    object InitScreen

    @Serializable
    object LoginScreen

    @Serializable
    object RegisterScreen

    @Serializable
    object TasksListScreen

    @Serializable
    data class AddTask(
        val id: Long?
    )
}