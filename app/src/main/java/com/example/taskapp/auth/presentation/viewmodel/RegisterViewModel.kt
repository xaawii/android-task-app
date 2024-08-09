package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.RegisterUseCase
import com.example.taskapp.auth.presentation.state.RegisterUIState
import com.example.taskapp.core.domain.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) :
    ViewModel() {
    private var _uiState = MutableStateFlow<RegisterUIState>(RegisterUIState.Editing())
    val uiState: StateFlow<RegisterUIState> = _uiState

    //event for error on register
    private val _registerErrorEvent = MutableSharedFlow<Boolean>()
    val registerErrorEvent: SharedFlow<Boolean> = _registerErrorEvent

    fun register() {
        viewModelScope.launch {

            if (_uiState.value is RegisterUIState.Editing) {

                val uiStateTemporal = _uiState.value as RegisterUIState.Editing

                _uiState.value = RegisterUIState.Loading

                try {
                    val user = UserModel(
                        id = 0,
                        email = uiStateTemporal.email,
                        name = "",
                        password = uiStateTemporal.password,
                        role = "ROLE_USER"
                    )

                    registerUseCase(user)

                    _uiState.value = RegisterUIState.Success

                } catch (e: Exception) {
                    if (e.message!!.contains("already in use")) {
                        _registerErrorEvent.emit(true)
                    } else {
                        _registerErrorEvent.emit(false)
                    }

                    _uiState.value = RegisterUIState.Editing()
                }

            }
        }
    }


    fun onEmailChanged(email: String) {
        if (_uiState.value is RegisterUIState.Editing) {
            _uiState.value = (_uiState.value as RegisterUIState.Editing).copy(email = email)
        }
        checkFormIsValid()
    }

    fun onNameChanged(name: String) {
        if (_uiState.value is RegisterUIState.Editing) {
            _uiState.value = (_uiState.value as RegisterUIState.Editing).copy(name = name)
        }
        checkFormIsValid()
    }

    fun onPasswordChanged(password: String) {
        if (_uiState.value is RegisterUIState.Editing) {
            _uiState.value = (_uiState.value as RegisterUIState.Editing).copy(password = password)
        }
        checkPasswordMatch()
        checkFormIsValid()
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        if (_uiState.value is RegisterUIState.Editing) {
            _uiState.value =
                (_uiState.value as RegisterUIState.Editing).copy(confirmPassword = confirmPassword)
        }

        checkPasswordMatch()
        checkFormIsValid()
    }

    private fun checkPasswordMatch() {
        if (_uiState.value is RegisterUIState.Editing) {
            val uiStateTemp = (_uiState.value as RegisterUIState.Editing)
            if (uiStateTemp.password == uiStateTemp.confirmPassword) {
                _uiState.value =
                    (_uiState.value as RegisterUIState.Editing).copy(passwordMatch = true)
            } else {
                _uiState.value =
                    (_uiState.value as RegisterUIState.Editing).copy(passwordMatch = false)
            }
        }
    }

    private fun checkFormIsValid() {

        val emailRegex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"
        val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$"

        if (_uiState.value is RegisterUIState.Editing) {
            val uiStateTemp = (_uiState.value as RegisterUIState.Editing)

            if (uiStateTemp.email.matches(Regex(emailRegex)) && uiStateTemp.email.length >= 3 && uiStateTemp.password.matches(
                    Regex(passwordRegex)
                ) && uiStateTemp.passwordMatch
            ) {
                _uiState.value =
                    (_uiState.value as RegisterUIState.Editing).copy(formIsValid = true)
            } else {
                _uiState.value =
                    (_uiState.value as RegisterUIState.Editing).copy(formIsValid = false)
            }
        }
    }
}