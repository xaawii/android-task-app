package com.example.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskapp.core.routes.Routes
import com.example.taskapp.task.presentation.screens.AddTaskScreen
import com.example.taskapp.task.presentation.screens.TaskScreen
import com.example.taskapp.task.presentation.viewmodel.AddTaskViewModel
import com.example.taskapp.task.presentation.viewmodel.TaskListViewModel
import com.example.taskapp.ui.theme.TaskAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val taskListViewModel: TaskListViewModel by viewModels()
    private val addTaskViewModel: AddTaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navigationController = rememberNavController()
                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.TasksListScreen.route
                    ) {

                        composable(Routes.TasksListScreen.route) {
                            TaskScreen(
                                taskListViewModel,
                                navigationController
                            )
                        }

                        composable(
                            Routes.AddTask.route,
                            arguments = listOf(navArgument("taskId") { type= NavType.LongType })
                        ) {
                            AddTaskScreen(
                                navigationController = navigationController,
                                addTaskViewModel,
                                it.arguments?.getLong("taskId") ?: 0L
                            )
                        }


                    }

                }
            }
        }
    }
}

