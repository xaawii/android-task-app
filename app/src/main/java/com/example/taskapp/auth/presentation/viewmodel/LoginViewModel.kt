package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.LoginUseCase
import com.example.taskapp.auth.domain.usecases.SaveTokenUseCase
import com.example.taskapp.auth.domain.usecases.SaveUserDataUseCase
import com.example.taskapp.auth.presentation.state.LoginUIState
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.usecases.GetUserInfoByEmailUseCase
import com.example.taskapp.core.domain.validator.Result
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
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveUserDataUseCase: SaveUserDataUseCase,
    private val getUserInfoByEmailUseCase: GetUserInfoByEmailUseCase,
) : ViewModel() {

    private var _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Editing())
    val uiState: StateFlow<LoginUIState> = _uiState

    //event for error on login
    private val _loginErrorEvent = MutableSharedFlow<UiText>()
    val loginErrorEvent: SharedFlow<UiText> = _loginErrorEvent

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
                    is Result.Error -> emitError(result.error.asUiText())

                    is Result.Success -> {
                        saveTokenUseCase(result.data.token)
                        when (val resultUser = getUserInfoByEmailUseCase(email)) {
                            is Result.Error -> emitError(resultUser.error.asUiText())
                            is Result.Success -> {
                                saveUserDataUseCase(resultUser.data)
                                _uiState.value = LoginUIState.Success
                            }
                        }

                    }
                }

            }
        }
    }

    private suspend fun emitError(text: UiText) {
        _loginErrorEvent.emit(text)
        _uiState.value = LoginUIState.Editing()
    }


    fun onEmailChanged(email: String) {
        if (_uiState.value is LoginUIState.Editing) {
            _uiState.value = (_uiState.value as LoginUIState.Editing).copy(email = email)
        }
        checkFormIsValid()
    }

    fun onPasswordChanged(password: String) {
        if (_uiState.value is LoginUIState.Editing) {
            _uiState.value = (_uiState.value as LoginUIState.Editing).copy(password = password)
        }
        checkFormIsValid()
    }

    private fun checkFormIsValid() {
        (_uiState.value as? LoginUIState.Editing)?.apply {
            val formValid = email.isNotBlank() && password.isNotBlank()
            _uiState.value = copy(formIsValid = formValid)
        }
    }

    fun resetState() {
        _uiState.value = LoginUIState.Editing()
    }


}