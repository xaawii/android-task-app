package com.example.taskapp.auth.domain.usecases

import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import javax.inject.Inject

class SendNewPasswordAndSecurityCodeUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        token: String,
        newPassword: String
    ): Result<Unit, DataError.Network> {
        return authRepository.resetPassword(token, newPassword)
    }
}