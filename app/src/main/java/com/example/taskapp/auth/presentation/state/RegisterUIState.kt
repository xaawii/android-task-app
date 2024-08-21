package com.example.taskapp.auth.presentation.state

import com.example.taskapp.core.presentation.utils.UiText

sealed class RegisterUIState {
    data object Loading : RegisterUIState()
    data class Editing(
        val email: String = "",
        val name: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val passwordMatch: Boolean = false,
        val emailIsValid: Boolean = false,
        val nameIsValid: Boolean = false,
        val passwordIsValid: Boolean = false,
        val emailError: UiText? = null,
        val nameError: UiText? = null,
        val passwordError: UiText? = null,
        val formIsValid: Boolean = false
    ) : RegisterUIState()

    data class Error(val message: String) : RegisterUIState()
}