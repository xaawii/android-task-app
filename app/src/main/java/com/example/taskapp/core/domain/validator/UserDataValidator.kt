package com.example.taskapp.core.domain.validator

import javax.inject.Inject

class UserDataValidator @Inject constructor(){


    fun validatePassword(password: String): Result<Unit, PasswordError> {
        if (password.length < 6) return Result.Error(PasswordError.TOO_SHORT)


        val hasUppercaseChar = password.any { it.isUpperCase() }
        if (!hasUppercaseChar) return Result.Error(PasswordError.NO_UPPERCASE)

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) return Result.Error(PasswordError.NO_DIGIT)

        return Result.Success(Unit)
    }

    fun validateEmail(email: String): Result<Unit, EmailError> {
        val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
        if (!email.matches(Regex(emailRegex))) return Result.Error(EmailError.INVALID_FORMAT)
        return Result.Success(Unit)
    }

    fun validateName(name: String): Result<Unit, NameError> {
        if (name.length < 3) return Result.Error(NameError.TOO_SHORT)
        return Result.Success(Unit)
    }

    enum class PasswordError : Error {
        TOO_SHORT,
        NO_UPPERCASE,
        NO_DIGIT
    }

    enum class EmailError : Error {
        INVALID_FORMAT
    }

    enum class NameError : Error {
        TOO_SHORT
    }
}