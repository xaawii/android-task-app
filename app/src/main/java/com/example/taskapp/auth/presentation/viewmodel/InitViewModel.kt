package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.auth.domain.usecases.DeleteUserDataFromPreferencesUseCase
import com.example.taskapp.auth.domain.usecases.GetUserTokenUseCase
import com.example.taskapp.auth.presentation.state.InitUIState
import com.example.taskapp.core.domain.usecases.ValidateTokenUseCase
import com.example.taskapp.core.domain.validator.Result
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

                when (validateTokenUseCase(token)) {
                    is Result.Error -> {
                        removeUserData()
                        _uiState.value = InitUIState.Unauthenticated
                    }

                    is Result.Success -> _uiState.value = InitUIState.Authenticated
                }


        }
    }

    private fun removeUserData() {
        viewModelScope.launch {
            deleteUserDataFromPreferencesUseCase.invoke()
        }
    }
}