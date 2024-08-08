package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.DeleteUserDataFromPreferencesUseCase
import com.example.taskapp.auth.domain.usecases.GetUserTokenUseCase
import com.example.taskapp.auth.presentation.state.InitUIState
import com.example.taskapp.core.data.exceptions.InvalidTokenException
import com.example.taskapp.core.domain.usecases.ValidateTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
    private val getUserTokenUseCase: GetUserTokenUseCase,
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val deleteUserDataFromPreferencesUseCase: DeleteUserDataFromPreferencesUseCase
) :
    ViewModel() {
    private var _uiState = MutableStateFlow<InitUIState>(InitUIState.Loading)
    val uiState: StateFlow<InitUIState> = _uiState

    fun isLogged() {
        viewModelScope.launch {

            val token = getUserTokenUseCase.invoke()
            if (token.isNullOrBlank()) _uiState.value = InitUIState.Unauthenticated
            else
                try {
                    validateTokenUseCase(token)
                    _uiState.value = InitUIState.Authenticated
                } catch (e: InvalidTokenException) {
                    _uiState.value = InitUIState.Unauthenticated
                }


        }
    }

    fun removeUserData() {
        viewModelScope.launch {
            deleteUserDataFromPreferencesUseCase.invoke()
        }
    }
}