package com.example.taskapp.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
import com.example.taskapp.auth.presentation.components.RecoverFormStructure
import com.example.taskapp.auth.presentation.state.RecoverPasswordUIState
import com.example.taskapp.auth.presentation.viewmodel.RecoverPasswordViewModel
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.MyFormTextField
import com.example.taskapp.core.presentation.components.SuccessComponent
import com.example.taskapp.core.presentation.components.TopAppBarBack
import com.example.taskapp.core.routes.Routes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecoverPasswordScreen(
    recoverPasswordViewModel: RecoverPasswordViewModel,
    navigationController: NavHostController
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        recoverPasswordViewModel.resetState()
    }

    LaunchedEffect(Unit) {
        recoverPasswordViewModel.errorEvent.collectLatest {
            Toast.makeText(context, it.asString(context), Toast.LENGTH_SHORT).show()
        }
    }

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

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBarBack(
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
                    viewModel = recoverPasswordViewModel
                )

                is RecoverPasswordUIState.SendEmail -> SendEmailComponent(
                    uiState = uiState,
                    viewModel = recoverPasswordViewModel
                )

                is RecoverPasswordUIState.SendPassword -> ChangePasswordComponent(
                    uiState = uiState,
                    viewModel = recoverPasswordViewModel
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

@Composable
fun SendEmailComponent(
    uiState: RecoverPasswordUIState.SendEmail,
    viewModel: RecoverPasswordViewModel
) {

    RecoverFormStructure(
        title = stringResource(R.string.send_security_code_to_email),
        bodyText = stringResource(R.string.write_down_your_email_if_the_email_is_registered_you_will_receive_a_security_code),
        buttonEnabled = uiState.emailIsValid,
        onClickButton = viewModel::sendEmail,
        content = {
            MyFormTextField(
                label = stringResource(id = R.string.email),
                value = uiState.email,
                isValid = uiState.emailIsValid,
                keyboardType = KeyboardType.Email,
                errorMessage = uiState.emailError?.asString() ?: "",
                onValueChange = viewModel::onEmailChanged
            )
        })

}

@Composable
fun ValidateCodeComponent(
    uiState: RecoverPasswordUIState.SendCode,
    viewModel: RecoverPasswordViewModel
) {

    RecoverFormStructure(
        title = stringResource(R.string.verify_security_code),
        bodyText = stringResource(R.string.enter_the_security_code_that_you_should_have_received_at_the_indicated_email),
        buttonEnabled = uiState.codeIsValid,
        onClickButton = viewModel::sendCode,
        content = {
            MyFormTextField(
                label = stringResource(R.string.security_code),
                isValid = uiState.codeIsValid,
                value = uiState.code,
                keyboardType = KeyboardType.Text,
                onValueChange = viewModel::onCodeChanged
            )
            if (uiState.invalidCode) {
                Text(
                    text = stringResource(R.string.invalid_code),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                )
            }
        }
    )

}

@Composable
fun ChangePasswordComponent(
    uiState: RecoverPasswordUIState.SendPassword,
    viewModel: RecoverPasswordViewModel
) {

    RecoverFormStructure(
        title = stringResource(R.string.change_password),
        bodyText = stringResource(R.string.enter_your_new_password),
        buttonEnabled = uiState.formIsValid,
        onClickButton = viewModel::sendPassword,
        content = {
            PasswordTextField(
                value = uiState.password,
                isValid = uiState.passwordIsValid,
                label = stringResource(id = R.string.password),
                errorMessage = uiState.passwordError?.asString() ?: "",
                onValueChange = viewModel::onPasswordChanged
            )
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextField(
                value = uiState.confirmPassword,
                isValid = uiState.passwordMatch,
                label = stringResource(id = R.string.confirm_password),
                onValueChange = viewModel::onConfirmPasswordChanged
            )
        }
    )

}

