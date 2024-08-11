package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.RegisterUseCase
import com.example.taskapp.auth.presentation.state.RegisterUIState
import com.example.taskapp.core.domain.model.UserModel
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
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val userDataValidator: UserDataValidator
) : ViewModel() {
    private var _uiState = MutableStateFlow<RegisterUIState>(RegisterUIState.Editing())
    val uiState: StateFlow<RegisterUIState> = _uiState

    //error event
    private val _errorEvent = MutableSharedFlow<UiText>()
    val errorEvent: SharedFlow<UiText> = _errorEvent

    //registered event
    private val _registeredEvent = MutableSharedFlow<Unit>()
    val registeredEvent: SharedFlow<Unit> = _registeredEvent


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
                    is Result.Error -> emitError(result.error.asUiText())
                    is Result.Success -> _registeredEvent.emit(Unit)
                }
            }
        }
    }


    private suspend fun emitError(text: UiText) {
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
            validatePasswordMatches(password, confirmPassword)
            validatePassword(password)
            checkFormIsValid()
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            _uiState.value = copy(confirmPassword = confirmPassword)

            validatePasswordMatches(password, confirmPassword)
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

    private fun validatePasswordMatches(password: String, confirmPassword: String) {
        when (val result = userDataValidator.validatePasswordMatches(password, confirmPassword)) {
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
            UserDataValidator.PasswordError.NO_MATCHES -> "Confirm password don't match"
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

    private fun checkFormIsValid() {
        (_uiState.value as? RegisterUIState.Editing)?.apply {
            val formValid = emailIsValid && nameIsValid && passwordIsValid && passwordMatch
            _uiState.value = copy(formIsValid = formValid)
        }
    }

    fun resetState() {
        _uiState.value = RegisterUIState.Editing()
    }
}