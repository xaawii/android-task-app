package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskapp.auth.domain.usecases.SendNewPasswordAndSecurityCodeUseCase
import com.example.taskapp.auth.domain.usecases.SendRecoverPasswordPetitionUseCase
import com.example.taskapp.auth.domain.usecases.ValidatePasswordSecurityCodeUseCase
import com.example.taskapp.auth.presentation.state.RecoverPasswordUIState
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.domain.validator.UserDataValidator
import com.example.taskapp.core.presentation.utils.UiText
import com.example.taskapp.core.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val sendRecoverPasswordPetitionUseCase: SendRecoverPasswordPetitionUseCase,
    private val validatePasswordSecurityCodeUseCase: ValidatePasswordSecurityCodeUseCase,
    private val sendNewPasswordAndSecurityCodeUseCase: SendNewPasswordAndSecurityCodeUseCase,
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private var _uiState =
        MutableStateFlow<RecoverPasswordUIState>(RecoverPasswordUIState.SendEmail())
    val uiState: StateFlow<RecoverPasswordUIState> = _uiState


    fun onEmailChanged(email: String) {
        (_uiState.value as? RecoverPasswordUIState.SendEmail)?.apply {
            _uiState.value = copy(email = email)
            validateEmail(email)

        }
    }

    fun onPasswordChanged(password: String) {
        (_uiState.value as? RecoverPasswordUIState.SendPassword)?.apply {
            _uiState.value = copy(password = password)
            validatePasswordMatches(password, confirmPassword)
            validatePassword(password)

        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        (_uiState.value as? RecoverPasswordUIState.SendPassword)?.apply {
            _uiState.value = copy(confirmPassword = confirmPassword)

            validatePasswordMatches(password, confirmPassword)

        }
    }

    private fun validatePassword(password: String) {
        when (val result = userDataValidator.validatePassword(password)) {
            is Result.Error -> handlePasswordError(result.error.asUiText())
            is Result.Success -> {
                changePasswordIsValid(true)
            }
        }
    }

    private fun validatePasswordMatches(password: String, confirmPassword: String) {
        when (val result = userDataValidator.validatePasswordMatches(password, confirmPassword)) {
            is Result.Error -> handlePasswordError(result.error.asUiText())
            is Result.Success -> {
                changePasswordIsValid(true)
            }
        }
        checkPasswordMatches()
    }

    private fun handlePasswordError(error: UiText) {
        changePasswordErrorMessage(error)
        changePasswordIsValid(false)
    }

    private fun changePasswordErrorMessage(errorMessage: UiText) {
        (_uiState.value as? RecoverPasswordUIState.SendPassword)?.apply {
            _uiState.value = copy(passwordError = errorMessage)
        }
    }

    private fun changePasswordIsValid(value: Boolean) {
        (_uiState.value as? RecoverPasswordUIState.SendPassword)?.apply {
            _uiState.value = copy(passwordIsValid = value)
        }
    }

    private fun checkPasswordMatches() {
        (_uiState.value as? RecoverPasswordUIState.SendPassword)?.apply {
            _uiState.value = copy(passwordMatch = password == confirmPassword)
        }
    }

    private fun validateEmail(email: String) {
        when (val result = userDataValidator.validateEmail(email)) {
            is Result.Error -> handleEmailError(result.error.asUiText())
            is Result.Success -> {
                changeEmailIsValid(true)
            }
        }
    }

    private fun handleEmailError(error: UiText) {
        changeEmailErrorMessage(errorMessage = error)
        changeEmailIsValid(false)
    }

    private fun changeEmailErrorMessage(errorMessage: UiText) {
        (_uiState.value as? RecoverPasswordUIState.SendEmail)?.apply {
            _uiState.value = copy(emailError = errorMessage)
        }
    }

    private fun changeEmailIsValid(value: Boolean) {
        (_uiState.value as? RecoverPasswordUIState.SendEmail)?.apply {
            _uiState.value = copy(emailIsValid = value)
        }
    }


}