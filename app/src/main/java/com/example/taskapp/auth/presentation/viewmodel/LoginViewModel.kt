package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.LoginUseCase
import com.example.taskapp.auth.domain.usecases.SaveUserDataUseCase
import com.example.taskapp.auth.presentation.state.LoginUIState
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Editing())
    val uiState: StateFlow<LoginUIState> = _uiState

    //event for error on login
    private val _loginErrorEvent = MutableSharedFlow<String>()
    val loginErrorEvent: SharedFlow<String> = _loginErrorEvent

    fun login() {
        viewModelScope.launch {

            (_uiState.value as? LoginUIState.Editing)?.apply {

                _uiState.value = LoginUIState.Loading


                val user = UserModel(
                    id = 0,
                    email = email,
                    name = "",
                    password = password,
                    role = ""
                )

                when (val result = loginUseCase(user)) {
                    is Result.Error -> handleLoginError(result.error)

                    is Result.Success -> {
                        saveUserDataUseCase(result.data.token, user)
                        _uiState.value = LoginUIState.Success
                    }
                }

            }
        }
    }

    private suspend fun handleLoginError(error: DataError) {
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
        _loginErrorEvent.emit(text)
        _uiState.value = LoginUIState.Editing()
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