package com.example.taskapp.auth.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.auth.presentation.state.LoginUIState
import com.example.taskapp.auth.presentation.viewmodel.LoginViewModel
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.routes.Routes

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navigationController: NavHostController) {


    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<LoginUIState>(
        initialValue = LoginUIState.Editing(),
        key1 = lifecycle,
        key2 = loginViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            loginViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is LoginUIState.Editing -> {
            MainBody(uiState as LoginUIState.Editing, loginViewModel)
        }

        is LoginUIState.Error -> {
            Text("Error: ${(uiState as LoginUIState.Error).message}")
        }

        LoginUIState.Loading -> {
            LoadingComponent()
        }

        LoginUIState.Success -> {
            navigationController.navigate(Routes.TasksListScreen.route)
        }
    }
}

@Composable
private fun MainBody(uiState: LoginUIState.Editing, loginViewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.email,
            onValueChange = loginViewModel::onEmailChanged,
            label = { Text("Email") },
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            value = uiState.password,
            onValueChange = loginViewModel::onPasswordChanged,
            label = { Text("Password") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { loginViewModel.login(uiState.email, uiState.password) }) {
            Text(text = "Login")
        }
    }
}