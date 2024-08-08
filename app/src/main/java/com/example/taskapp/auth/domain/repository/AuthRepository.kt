package com.example.taskapp.auth.domain.repository

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.core.domain.model.UserModel

interface AuthRepository {

    suspend fun register(userModel: UserModel)
    suspend fun login(userModel: UserModel) : TokenDto
}