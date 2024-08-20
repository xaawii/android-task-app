package com.example.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.taskapp.auth.presentation.screens.InitScreen
import com.example.taskapp.auth.presentation.screens.LoginScreen
import com.example.taskapp.auth.presentation.screens.RecoverPasswordScreen
import com.example.taskapp.auth.presentation.screens.RegisterScreen
import com.example.taskapp.auth.presentation.viewmodel.InitViewModel
import com.example.taskapp.auth.presentation.viewmodel.LoginViewModel
import com.example.taskapp.auth.presentation.viewmodel.RecoverPasswordViewModel
import com.example.taskapp.auth.presentation.viewmodel.RegisterViewModel
import com.example.taskapp.core.routes.Routes
import com.example.taskapp.task.presentation.screens.AddTaskScreen
import com.example.taskapp.task.presentation.screens.DetailScreen
import com.example.taskapp.task.presentation.screens.TaskScreen
import com.example.taskapp.task.presentation.viewmodel.AddTaskViewModel
import com.example.taskapp.task.presentation.viewmodel.DetailViewModel
import com.example.taskapp.task.presentation.viewmodel.TaskListViewModel
import com.example.taskapp.ui.theme.NewAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val initViewModel: InitViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val taskListViewModel: TaskListViewModel by viewModels()
    private val addTaskViewModel: AddTaskViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val detailViewModel: DetailViewModel by viewModels()
    private val recoverPasswordViewModel: RecoverPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController = rememberNavController()


                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.InitScreen
                    ) {

                        composable<Routes.InitScreen> {
                            InitScreen(
                                initViewModel = initViewModel,
                                navigationController = navigationController
                            )
                        }

                        composable<Routes.LoginScreen> {
                            LoginScreen(loginViewModel, navigationController)
                        }

                        composable<Routes.RegisterScreen> {
                            RegisterScreen(registerViewModel, navigationController)
                        }

                        composable<Routes.RecoverPasswordScreen> {
                            RecoverPasswordScreen(
                                recoverPasswordViewModel = recoverPasswordViewModel,
                                navigationController = navigationController
                            )
                        }

                        composable<Routes.TasksListScreen> {
                            TaskScreen(
                                taskListViewModel,
                                navigationController
                            )
                        }

                        composable<Routes.AddTaskScreen> {
                            val args = it.toRoute<Routes.AddTaskScreen>()
                            AddTaskScreen(
                                navigationController = navigationController,
                                addTaskViewModel,
                                taskId = args.id ?: 0L
                            )
                        }

                        composable<Routes.DetailScreen> {
                            val args = it.toRoute<Routes.DetailScreen>()
                            DetailScreen(
                                detailViewModel,
                                navigationController,
                                taskId = args.id
                            )
                        }

                    }

                }
            }
        }
    }
}
