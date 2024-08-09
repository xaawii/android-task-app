package com.example.taskapp.core.routes

sealed class Routes(val route: String) {
    data object InitScreen : Routes("init")
    data object LoginScreen : Routes("login")
    data object RegisterScreen : Routes("register")
    data object TasksListScreen : Routes("tasks-screen")
    data object AddTask : Routes("add-task-screen?taskId={taskId}") {
        fun createRoute(taskId: Long) = "add-task-screen?taskId=$taskId"
    }
}