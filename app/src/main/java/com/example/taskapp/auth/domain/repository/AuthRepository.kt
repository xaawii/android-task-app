package com.example.taskapp.auth.domain.repository

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result

interface AuthRepository {

    suspend fun register(userModel: UserModel): Result<Unit, DataError.Network>
    suspend fun login(userModel: UserModel): Result<TokenDto, DataError.Network>
    suspend fun forgotPassword(email: String): Result<Unit, DataError.Network>
    suspend fun resetPassword(token: String, newPassword: String): Result<Unit, DataError.Network>
}