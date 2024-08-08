package com.example.taskapp.core.data.repository

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.core.data.exceptions.InvalidTokenException
import com.example.taskapp.core.data.remote.api.TokenApiClient
import com.example.taskapp.core.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(private val tokenApiClient: TokenApiClient) :
    TokenRepository {
    override suspend fun validateToken(token: String): TokenDto {
        return withContext(Dispatchers.IO) {
            val result = tokenApiClient.validateToken(token)
            if (result.isSuccessful && result.body() != null) {
                result.body()!!
            } else
                throw InvalidTokenException()
        }
    }
}