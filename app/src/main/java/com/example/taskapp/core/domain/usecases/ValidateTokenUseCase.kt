package com.example.taskapp.core.domain.usecases

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.core.domain.repository.TokenRepository
import javax.inject.Inject

class ValidateTokenUseCase @Inject constructor(private val tokenRepository: TokenRepository) {
    suspend operator fun invoke(token: String): TokenDto {
        return tokenRepository.validateToken(token)
    }
}