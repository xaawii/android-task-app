package com.example.taskapp.auth.presentation.state

sealed class RecoverPasswordUIState {
    data object Loading : RecoverPasswordUIState()

    data class SendEmail(
        val email: String = "",
        val emailIsValid: Boolean = false
    ) : RecoverPasswordUIState()

    data class SendCode(
        val code: String = "",
        val codeIsValid: Boolean = false
    ) : RecoverPasswordUIState()

    data class SendPassword(
        val code: String,
        val password: String = "",
        val confirmPassword: String = "",
        val passwordError: String = "",
        val passwordIsValid: Boolean = false,
        val passwordMatch: Boolean = false,
        val formIsValid: Boolean = false
    ) : RecoverPasswordUIState()

    data object Success : RecoverPasswordUIState()
}