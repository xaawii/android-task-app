package com.example.taskapp.auth.presentation.state

sealed class InitUIState {

    data object Loading : InitUIState()
    data object Authenticated : InitUIState()
    data object Unauthenticated : InitUIState()
    data class Error(val message: String) : InitUIState()
}