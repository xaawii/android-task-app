package com.example.taskapp.core.domain.usecases

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.core.domain.repository.TokenRepository
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import javax.inject.Inject

class ValidateTokenUseCase @Inject constructor(private val tokenRepository: TokenRepository) {
    suspend operator fun invoke(token: String): Result<TokenDto, DataError.Network> {
        return tokenRepository.validateToken(token)
    }
}