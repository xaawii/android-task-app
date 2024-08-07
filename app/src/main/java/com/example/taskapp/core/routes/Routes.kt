package com.example.taskapp.core.routes

sealed class Routes(val route: String) {
    object TasksListScreen : Routes("tasks-screen")
    object AddTask : Routes("add-task-screen?taskId={taskId}") {
        fun createRoute(taskId: Long) = "add-task-screen?taskId=$taskId"
    }
}