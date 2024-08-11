package com.example.taskapp.auth.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.auth.presentation.state.InitUIState
import com.example.taskapp.auth.presentation.viewmodel.InitViewModel
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.routes.Routes

@Composable
fun InitScreen(initViewModel: InitViewModel, navigationController: NavHostController) {


    LaunchedEffect(true) {
        initViewModel.isLogged()
    }


    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<InitUIState>(
        initialValue = InitUIState.Loading,
        key1 = lifecycle,
        key2 = initViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            initViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        InitUIState.Authenticated -> {
            navigationController.navigate(Routes.TasksListScreen.route)
        }

        is InitUIState.Error -> {
            Text("Error: ${(uiState as InitUIState.Error).message}")
        }

        InitUIState.Loading -> {
            LoadingComponent()
        }

        InitUIState.Unauthenticated -> {
            navigationController.navigate(Routes.LoginScreen.route)
        }
    }


}