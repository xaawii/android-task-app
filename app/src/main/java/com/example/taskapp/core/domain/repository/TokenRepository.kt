package com.example.taskapp.core.domain.repository

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result

interface TokenRepository {
    suspend fun validateToken(token: String): Result<TokenDto, DataError.Network>
}