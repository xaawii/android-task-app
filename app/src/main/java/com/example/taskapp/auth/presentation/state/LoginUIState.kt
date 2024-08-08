package com.example.taskapp.auth.presentation.state

sealed class LoginUIState {

    data object Loading : LoginUIState()
    data object Success : LoginUIState()
    data class Editing(
        val email: String = "",
        val password: String = ""
    ) : LoginUIState()

    data class Error(val message: String) : LoginUIState()
}