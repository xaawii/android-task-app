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
    object RecoverPasswordScreen

    @Serializable
    object TasksListScreen

    @Serializable
    data class AddTaskScreen(
        val id: Long?
    )

    @Serializable
    data class DetailScreen(
        val id: Long
    )
}