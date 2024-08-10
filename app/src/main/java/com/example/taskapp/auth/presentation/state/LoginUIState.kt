package com.example.taskapp.auth.presentation.state

sealed class LoginUIState {

    data object Loading : LoginUIState()
    data object Success : LoginUIState()
    data class Editing(
        val email: String = "",
        val password: String = "",
        val formIsValid: Boolean = false
    ) : LoginUIState()

    data class Error(val message: String) : LoginUIState()
}