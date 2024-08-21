package com.example.taskapp.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import com.example.taskapp.auth.presentation.state.RegisterUIState
import com.example.taskapp.auth.presentation.viewmodel.RegisterViewModel
import com.example.taskapp.core.presentation.components.CircleBackground
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.MyFormTextField
import com.example.taskapp.core.presentation.components.SuccessComponent
import com.example.taskapp.core.routes.Routes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navigationController: NavHostController) {

    val context = LocalContext.current

    val lifecycle = LocalLifecycleOwner.current.lifecycle


    LaunchedEffect(Unit) {
        registerViewModel.resetState()
    }

    LaunchedEffect(Unit) {
        registerViewModel.errorEvent.collectLatest {
            Toast.makeText(context, it.asString(context), Toast.LENGTH_SHORT).show()
        }
    }

    val uiState by produceState<RegisterUIState>(
        initialValue = RegisterUIState.Editing(),
        key1 = lifecycle,
        key2 = registerViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            registerViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is RegisterUIState.Editing -> {
            MainBody(uiState as RegisterUIState.Editing, registerViewModel, navigationController)
        }

        is RegisterUIState.Error -> {
            Text("Error: ${(uiState as RegisterUIState.Error).message}")
        }

        RegisterUIState.Loading -> {
            LoadingComponent()
        }

        RegisterUIState.Success -> {
            SuccessComponent(
                text = stringResource(R.string.account_created),
                onFinish = {
                    navigationController.navigate(Routes.LoginScreen) {
                        popUpTo(Routes.RegisterScreen) { inclusive = true }
                    }
                })
        }
    }
}

@Composable
private fun MainBody(
    uiState: RegisterUIState.Editing,
    registerViewModel: RegisterViewModel,
    navigationController: NavHostController
) {
    CircleBackground(color = MaterialTheme.colorScheme.primary) {
        Scaffold(
            containerColor = Color.Transparent,
        ) { contentPadding ->


            RegisterBody(contentPadding, uiState, registerViewModel, goToLogin = {
                navigationController.navigate(Routes.LoginScreen) {
                    popUpTo(Routes.RegisterScreen) { inclusive = true }
                }
            })


        }
    }

}


@Composable
private fun RegisterBody(
    paddingValues: PaddingValues,
    uiState: RegisterUIState.Editing,
    registerViewModel: RegisterViewModel,
    goToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.create_an_account),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //email
            MyFormTextField(
                label = stringResource(R.string.email),
                value = uiState.email,
                isValid = uiState.emailIsValid,
                errorMessage = uiState.emailError?.asString() ?: "",
                keyboardType = KeyboardType.Email,
                onValueChange = registerViewModel::onEmailChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            //name
            MyFormTextField(
                label = stringResource(R.string.name),
                value = uiState.name,
                isValid = uiState.nameIsValid,
                errorMessage = uiState.nameError?.asString() ?: "",
                keyboardType = KeyboardType.Text,
                onValueChange = registerViewModel::onNameChanged
            )

            Spacer(modifier = Modifier.height(16.dp))


            //password
            PasswordTextField(
                value = uiState.password,
                label = stringResource(R.string.password),
                isValid = uiState.passwordIsValid,
                errorMessage = uiState.passwordError?.asString() ?: "",
                onValueChange = registerViewModel::onPasswordChanged
            )
            Spacer(modifier = Modifier.height(16.dp))

            //confirm password
            PasswordTextField(
                value = uiState.confirmPassword,
                label = stringResource(R.string.confirm_password),
                isValid = uiState.passwordMatch,
                onValueChange = registerViewModel::onConfirmPasswordChanged
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = registerViewModel::register,
                enabled = uiState.formIsValid
            ) {
                Text(
                    text = stringResource(R.string.sign_up),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .weight(1F),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = stringResource(R.string.already_have_an_account),
                style = MaterialTheme.typography.bodySmall
            )
            TextButton(
                onClick = goToLogin
            ) {
                Text(
                    text = stringResource(R.string.sign_in),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

