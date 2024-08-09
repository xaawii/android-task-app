package com.example.taskapp.auth.presentation.state

sealed class RegisterUIState {
    data object Loading : RegisterUIState()
    data object Success : RegisterUIState()
    data class Editing(
        val email: String = "",
        val name: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val passwordMatch: Boolean = false,
        val formIsValid: Boolean = false
    ) : RegisterUIState()

    data class Error(val message: String) : RegisterUIState()
}