package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.LoginUseCase
import com.example.taskapp.auth.presentation.state.LoginUIState
import com.example.taskapp.core.domain.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private var _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Editing())
    val uiState: StateFlow<LoginUIState> = _uiState

    fun login() {
        viewModelScope.launch {

            if (_uiState.value == LoginUIState.Editing()) {

                val uiStateTemporal = _uiState.value as LoginUIState.Editing

                _uiState.value = LoginUIState.Loading

                try {
                    val user = UserModel(
                        id = 0,
                        email = uiStateTemporal.email,
                        name = "",
                        password = uiStateTemporal.password,
                        role = ""
                    )

                    loginUseCase(user)

                    _uiState.value = LoginUIState.Success

                } catch (e: Exception) {
                    _uiState.value = LoginUIState.Error("Error: ${e.message}")
                }

            }
        }
    }


    fun onEmailChanged(email: String) {
        if (_uiState.value is LoginUIState.Editing) {
            _uiState.value = (_uiState.value as LoginUIState.Editing).copy(email = email)
        }
    }

    fun onPasswordChanged(password: String) {
        if (_uiState.value is LoginUIState.Editing) {
            _uiState.value = (_uiState.value as LoginUIState.Editing).copy(password = password)
        }
    }
}