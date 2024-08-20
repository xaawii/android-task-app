package com.example.taskapp.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.taskapp.auth.presentation.state.RecoverPasswordUIState
import com.example.taskapp.auth.presentation.viewmodel.RecoverPasswordViewModel
import com.example.taskapp.core.presentation.components.CircleBackground
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.MyFormTextField
import com.example.taskapp.core.presentation.components.SuccessComponent
import com.example.taskapp.core.presentation.components.TopAppBarBack
import com.example.taskapp.core.routes.Routes

@Composable
fun RecoverPasswordScreen(
    recoverPasswordViewModel: RecoverPasswordViewModel,
    navigationController: NavHostController
) {

    val context = LocalContext.current

    val lifecycle = LocalLifecycleOwner.current.lifecycle


    val uiState by produceState<RecoverPasswordUIState>(
        initialValue = RecoverPasswordUIState.SendEmail(),
        key1 = lifecycle,
        key2 = recoverPasswordViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            recoverPasswordViewModel.uiState.collect { value = it }
        }
    }

    MainBody(
        uiState = uiState,
        recoverPasswordViewModel = recoverPasswordViewModel,
        navigationController = navigationController
    )
}

@Composable
private fun MainBody(
    uiState: RecoverPasswordUIState,
    recoverPasswordViewModel: RecoverPasswordViewModel,
    navigationController: NavHostController
) {
    CircleBackground(color = MaterialTheme.colorScheme.primary) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBarBack(
                    title = stringResource(
                        R.string.recover_password
                    ),
                    titleStyle = MaterialTheme.typography.titleSmall,
                    onBackPressed = navigationController::popBackStack
                )
            },
        ) { contentPadding ->

            Box(
                Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                when (uiState) {
                    RecoverPasswordUIState.Loading -> LoadingComponent()
                    is RecoverPasswordUIState.SendCode -> ValidateCodeComponent(
                        uiState = uiState,
                        recoverPasswordViewModel = recoverPasswordViewModel
                    )

                    is RecoverPasswordUIState.SendEmail -> SendCodeComponent(
                        uiState = uiState,
                        recoverPasswordViewModel = recoverPasswordViewModel
                    )

                    is RecoverPasswordUIState.SendPassword -> ChangePasswordComponent(
                        uiState = uiState,
                        recoverPasswordViewModel = recoverPasswordViewModel
                    )

                    RecoverPasswordUIState.Success -> {
                        SuccessComponent(
                            text = stringResource(R.string.password_changed),
                            onFinish = {
                                navigationController.navigate(Routes.LoginScreen) {
                                    popUpTo(Routes.RecoverPasswordScreen) { inclusive = true }
                                }
                            })
                    }
                }
            }

        }
    }

}

@Composable
fun SendCodeComponent(
    uiState: RecoverPasswordUIState.SendEmail,
    recoverPasswordViewModel: RecoverPasswordViewModel
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(R.string.write_down_your_email_if_the_email_is_registered_you_will_receive_a_security_code))
        Spacer(modifier = Modifier.height(8.dp))
        MyFormTextField(
            label = stringResource(id = R.string.email),
            value = uiState.email,
            keyboardType = KeyboardType.Email,
            errorMessage = uiState.emailError?.asString() ?: "",
            onValueChange = recoverPasswordViewModel::onEmailChanged
        )
        Button(onClick = { /*TODO*/ }, enabled = uiState.emailIsValid) {
            Text(text = stringResource(R.string.send))
        }
    }

}

@Composable
fun ValidateCodeComponent(
    uiState: RecoverPasswordUIState.SendCode,
    recoverPasswordViewModel: RecoverPasswordViewModel
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(R.string.enter_the_security_code_that_you_should_have_received_at_the_indicated_email))
        Spacer(modifier = Modifier.height(8.dp))
        MyFormTextField(
            label = stringResource(R.string.security_code),
            value = uiState.code,
            keyboardType = KeyboardType.Text,
            onValueChange = {/*TODO*/ }
        )
        Button(onClick = { /*TODO*/ }, enabled = uiState.codeIsValid) {
            Text(text = stringResource(R.string.send))
        }
    }
}

@Composable
fun ChangePasswordComponent(
    uiState: RecoverPasswordUIState.SendPassword,
    recoverPasswordViewModel: RecoverPasswordViewModel
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(R.string.enter_your_new_password))
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            value = uiState.password,
            label = stringResource(id = R.string.password),
            errorMessage = uiState.passwordError?.asString() ?: "",
            onValueChange = recoverPasswordViewModel::onPasswordChanged
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordTextField(
            value = uiState.confirmPassword,
            label = stringResource(id = R.string.confirm_password),
            onValueChange = recoverPasswordViewModel::onConfirmPasswordChanged
        )
        Button(onClick = { /*TODO*/ }, enabled = uiState.formIsValid) {
            Text(text = stringResource(R.string.send))
        }
    }
}