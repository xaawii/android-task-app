package com.example.taskapp.core.domain.repository

import com.example.taskapp.auth.data.remote.dto.TokenDto

interface TokenRepository {
    suspend fun validateToken(token: String): TokenDto
}