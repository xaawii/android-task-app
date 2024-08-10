package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.RegisterUseCase
import com.example.taskapp.auth.presentation.state.RegisterUIState
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.domain.validator.UserDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val userDataValidator: UserDataValidator
) : ViewModel() {
    private var _uiState = MutableStateFlow<RegisterUIState>(RegisterUIState.Editing())
    val uiState: StateFlow<RegisterUIState> = _uiState

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent

    fun register() {
        viewModelScope.launch {
            (_uiState.value as? RegisterUIState.Editing)?.apply {
                _uiState.value = RegisterUIState.Loading

                val user = UserModel(
                    id = 0,
                    email = email,
                    name = "",
                    password = password,
                    role = "ROLE_USER"
                )

                when (val result = registerUseCase(user)) {
                    is Result.Error -> handleRegisterError(result.error)
                    is Result.Success -> _uiState.value = RegisterUIState.Success
                }
            }
        }
    }

    private suspend fun handleRegisterError(error: DataError) {
        val message = when (error) {
            DataError.Network.REQUEST_TIMEOUT -> "Request timeout"
            DataError.Network.TOO_MANY_REQUESTS -> "Too many requests"
            DataError.Network.NO_INTERNET -> "No internet"
            DataError.Network.SERVER_ERROR -> "Server error"
            DataError.Network.BAD_REQUEST -> "Email already in use"
            DataError.Network.UNAUTHORIZED -> "Unauthorized"
            else -> "Unknown error"
        }
        emitError(message)
    }

    private suspend fun emitError(text: String) {
        _errorEvent.emit(text)
        _uiState.value = RegisterUIState.Editing()
    }

    fun onEmailChanged(email: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(email = email)
            validateEmail(email)
            checkFormIsValid()
        }
    }

    fun onNameChanged(name: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(name = name)

            validateName(name)
            checkFormIsValid()

        }
    }

    fun onPasswordChanged(password: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(password = password)
            checkPasswordMatch()
            validatePassword(password)
            checkFormIsValid()
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(confirmPassword = confirmPassword)
            checkPasswordMatch()
            checkFormIsValid()
        }
    }

    private fun validatePassword(password: String) {
        when (val result = userDataValidator.validatePassword(password)) {
            is Result.Error -> handlePasswordError(result.error)
            is Result.Success -> {
                changePasswordErrorMessage("")
                changePasswordIsValid(true)
            }
        }
    }

    private fun handlePasswordError(error: UserDataValidator.PasswordError) {
        val message = when (error) {
            UserDataValidator.PasswordError.TOO_SHORT -> "Min 6 characters"
            UserDataValidator.PasswordError.NO_UPPERCASE -> "Needs at least one uppercase"
            UserDataValidator.PasswordError.NO_DIGIT -> "Needs at least one digit"
        }
        changePasswordErrorMessage(message)
        changePasswordIsValid(false)
    }

    private fun changePasswordErrorMessage(errorMessage: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(passwordError = errorMessage)
        }
    }

    private fun changePasswordIsValid(value: Boolean) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(passwordIsValid = value)
        }
    }

    private fun validateEmail(email: String) {
        when (val result = userDataValidator.validateEmail(email)) {
            is Result.Error -> handleEmailError(result.error)

            is Result.Success -> {
                changeEmailErrorMessage("")
                changeEmailIsValid(true)
            }
        }
    }

    private fun handleEmailError(error: UserDataValidator.EmailError) {
        if (error == UserDataValidator.EmailError.INVALID_FORMAT) {
            changeEmailErrorMessage("Invalid email format")
            changeEmailIsValid(false)
        }
    }

    private fun changeEmailErrorMessage(errorMessage: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(emailError = errorMessage)
        }
    }

    private fun changeEmailIsValid(value: Boolean) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(emailIsValid = value)
        }
    }

    private fun validateName(name: String) {
        when (val result = userDataValidator.validateName(name)) {
            is Result.Error -> handleNameError(result.error)
            is Result.Success -> {
                changeNameErrorMessage("")
                changeNameIsValid(true)
            }
        }
    }

    private fun handleNameError(error: UserDataValidator.NameError) {
        if (error == UserDataValidator.NameError.TOO_SHORT) {
            changeNameErrorMessage("Min 3 characters")
            changeNameIsValid(false)
        }
    }

    private fun changeNameErrorMessage(errorMessage: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(nameError = errorMessage)
        }
    }

    private fun changeNameIsValid(value: Boolean) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(nameIsValid = value)
        }
    }

    private fun checkPasswordMatch() {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(passwordMatch = password == confirmPassword)
        }
    }

    private fun checkFormIsValid() {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            val formValid = emailIsValid && nameIsValid && passwordIsValid && passwordMatch
            _uiState.value = copy(formIsValid = formValid)
        }
    }
}