package com.example.taskapp.auth.domain.usecases

import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import javax.inject.Inject

class SendRecoverPasswordPetitionUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String): Result<Unit, DataError.Network> {
        return authRepository.forgotPassword(email)
    }
}