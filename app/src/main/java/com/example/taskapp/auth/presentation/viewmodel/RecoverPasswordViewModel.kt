package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.SendNewPasswordAndSecurityCodeUseCase
import com.example.taskapp.auth.domain.usecases.SendRecoverPasswordPetitionUseCase
import com.example.taskapp.auth.domain.usecases.ValidatePasswordSecurityCodeUseCase
import com.example.taskapp.auth.presentation.state.RecoverPasswordUIState
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.domain.validator.UserDataValidator
import com.example.taskapp.core.presentation.utils.UiText
import com.example.taskapp.core.presentation.utils.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    //event for error
    private val _errorEvent = MutableSharedFlow<UiText>()
    val errorEvent: SharedFlow<UiText> = _errorEvent

    fun sendEmail() {
        viewModelScope.launch {
            (_uiState.value as? RecoverPasswordUIState.SendEmail)?.apply {

                _uiState.value = RecoverPasswordUIState.Loading

                when (val result = sendRecoverPasswordPetitionUseCase(email)) {
                    is Result.Error -> {
                        emitError(result.error.asUiText())
                        _uiState.value = RecoverPasswordUIState.SendEmail()
                    }

                    is Result.Success -> _uiState.value = RecoverPasswordUIState.SendCode()
                }

            }
        }
    }

    fun sendCode() {
        viewModelScope.launch {
            (_uiState.value as? RecoverPasswordUIState.SendCode)?.apply {
                _uiState.value = RecoverPasswordUIState.Loading

                when (val result = validatePasswordSecurityCodeUseCase(code)) {
                    is Result.Error -> {
                        when (result.error) {

                            DataError.Network.BAD_REQUEST -> _uiState.value =
                                RecoverPasswordUIState.SendCode(invalidCode = true)

                            DataError.Network.NOT_FOUND -> _uiState.value =
                                RecoverPasswordUIState.SendCode(invalidCode = true)

                            else -> {
                                emitError(result.error.asUiText())
                                _uiState.value = RecoverPasswordUIState.SendCode()
                            }
                        }
                    }

                    is Result.Success -> {
                        if (result.data) {
                            _uiState.value = RecoverPasswordUIState.SendPassword(code = code)
                        } else {
                            _uiState.value = RecoverPasswordUIState.SendCode(invalidCode = true)
                        }
                    }
                }
            }
        }
    }

    fun sendPassword() {
        viewModelScope.launch {
            (_uiState.value as? RecoverPasswordUIState.SendPassword)?.apply {
                _uiState.value = RecoverPasswordUIState.Loading

                when (val result =
                    sendNewPasswordAndSecurityCodeUseCase(token = code, newPassword = password)) {
                    is Result.Error -> {
                        emitError(result.error.asUiText())
                        _uiState.value = RecoverPasswordUIState.SendPassword(code)
                    }

                    is Result.Success -> _uiState.value = RecoverPasswordUIState.Success
                }
            }
        }
    }

    private suspend fun emitError(text: UiText) {
        _errorEvent.emit(text)
    }


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

    fun onCodeChanged(code: String) {
        (_uiState.value as? RecoverPasswordUIState.SendCode)?.apply {
            _uiState.value = copy(code = code, codeIsValid = code.isNotBlank())
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
        checkPasswordMatchesAndIsValid()
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

    private fun checkPasswordMatchesAndIsValid() {
        (_uiState.value as? RecoverPasswordUIState.SendPassword)?.apply {
            val passwordMatch = password == confirmPassword
            val formIsValid = passwordMatch && passwordIsValid
            _uiState.value = copy(passwordMatch = passwordMatch, formIsValid = formIsValid)
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

    fun resetState() {
        _uiState.value = RecoverPasswordUIState.SendEmail()
    }


}