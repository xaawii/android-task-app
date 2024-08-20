package com.example.taskapp.auth.presentation.state

import com.example.taskapp.core.presentation.utils.UiText

sealed class RecoverPasswordUIState {
    data object Loading : RecoverPasswordUIState()

    data class SendEmail(
        val email: String = "",
        val emailIsValid: Boolean = false,
        val emailError: UiText? = null
    ) : RecoverPasswordUIState()

    data class SendCode(
        val code: String = "",
        val codeIsValid: Boolean = false,
        val invalidCode: Boolean = false
    ) : RecoverPasswordUIState()

    data class SendPassword(
        val code: String,
        val password: String = "",
        val confirmPassword: String = "",
        val passwordError: UiText? = null,
        val passwordIsValid: Boolean = false,
        val passwordMatch: Boolean = false,
        val formIsValid: Boolean = false
    ) : RecoverPasswordUIState()

    data object Success : RecoverPasswordUIState()
}