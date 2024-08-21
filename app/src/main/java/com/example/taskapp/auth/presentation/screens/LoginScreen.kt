package com.example.taskapp.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.R
import com.example.taskapp.auth.presentation.components.PasswordTextField
import com.example.taskapp.auth.presentation.state.LoginUIState
import com.example.taskapp.auth.presentation.viewmodel.LoginViewModel
import com.example.taskapp.core.presentation.components.CircleBackground
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.MyFormTextField
import com.example.taskapp.core.routes.Routes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navigationController: NavHostController) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        loginViewModel.resetState()
    }

    LaunchedEffect(key1 = loginViewModel.loginErrorEvent) {
        loginViewModel.loginErrorEvent.collectLatest {
            Toast.makeText(context, it.asString(context), Toast.LENGTH_SHORT).show()
        }
    }


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
            MainBody(uiState as LoginUIState.Editing, loginViewModel, navigationController)
        }

        is LoginUIState.Error -> {
            Text("Error: ${(uiState as LoginUIState.Error).message}")
        }

        LoginUIState.Loading -> {
            LoadingComponent()
        }

        LoginUIState.Success -> {
            navigationController.navigate(Routes.TasksListScreen) {
                popUpTo(Routes.LoginScreen) { inclusive = true }
            }
        }
    }
}

@Composable
private fun MainBody(
    uiState: LoginUIState.Editing,
    loginViewModel: LoginViewModel,
    navigationController: NavHostController
) {

    CircleBackground(color = MaterialTheme.colorScheme.primary) {
        Scaffold(containerColor = Color.Transparent) { contentPadding ->

            LoginBody(contentPadding, uiState, loginViewModel, navigationController)


        }
    }

}

@Composable
private fun LoginBody(
    contentPadding: PaddingValues,
    uiState: LoginUIState.Editing,
    loginViewModel: LoginViewModel,
    navigationController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.log_in_to_start),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = contentPadding)
                .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MyFormTextField(
                label = stringResource(R.string.email),
                value = uiState.email,
                keyboardType = KeyboardType.Email,
                onValueChange = loginViewModel::onEmailChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = uiState.password,
                label = stringResource(R.string.password),
                onValueChange = loginViewModel::onPasswordChanged
            )

            Box(modifier = Modifier.fillMaxWidth(),
                Alignment.CenterEnd){
                TextButton(
                    onClick = { navigationController.navigate(Routes.RecoverPasswordScreen) }
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = loginViewModel::login,
                enabled = uiState.formIsValid
            ) {
                Text(
                    text = stringResource(R.string.sign_in),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.don_t_have_an_account),
                style = MaterialTheme.typography.bodySmall
            )
            TextButton(
                onClick = { navigationController.navigate(Routes.RegisterScreen) }
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }


    }
}

