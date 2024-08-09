package com.example.taskapp.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.auth.presentation.components.PasswordTextField
import com.example.taskapp.auth.presentation.state.RegisterUIState
import com.example.taskapp.auth.presentation.viewmodel.RegisterViewModel
import com.example.taskapp.core.presentation.components.LoadingComponent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, navigationController: NavHostController) {

    val context = LocalContext.current

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(key1 = registerViewModel.registerErrorEvent) {
        registerViewModel.registerErrorEvent.collectLatest {
            if (it) {
                Toast.makeText(context, "Email already in use", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to create account", Toast.LENGTH_SHORT).show()
            }
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
            MainBody(uiState as RegisterUIState.Editing, registerViewModel)
        }

        is RegisterUIState.Error -> {
            Text("Error: ${(uiState as RegisterUIState.Error).message}")
        }

        RegisterUIState.Loading -> {
            LoadingComponent()
        }

        RegisterUIState.Success -> {
            Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
            navigationController.popBackStack()
        }
    }
}

@Composable
private fun MainBody(uiState: RegisterUIState.Editing, registerViewModel: RegisterViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        //email
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.email,
            onValueChange = registerViewModel::onEmailChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            label = { Text("Email") },
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        //name
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = uiState.name,
            onValueChange = registerViewModel::onNameChanged,
            label = { Text("Name") },
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        //password
        PasswordTextField(
            text = uiState.password,
            label = "Password",
            onValueChange = registerViewModel::onPasswordChanged
        )
        Spacer(modifier = Modifier.height(16.dp))

        //confirm password
        PasswordTextField(
            text = uiState.confirmPassword,
            label = "Confirm password",
            onValueChange = registerViewModel::onConfirmPasswordChanged
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = registerViewModel::register, enabled = uiState.formIsValid) {
            Text(text = "Sign Up")
        }
    }
}

