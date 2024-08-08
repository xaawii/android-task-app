package com.example.taskapp.auth.data.repository

import com.example.taskapp.auth.data.remote.api.AuthApiClient
import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.auth.mappers.AuthDtoMapper
import com.example.taskapp.core.domain.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiClient: AuthApiClient,
    private val authDtoMapper: AuthDtoMapper
) : AuthRepository {
    override suspend fun register(userModel: UserModel) {
        return withContext(Dispatchers.IO) {
            val response =
                authApiClient.register(authDtoMapper.fromDomainToRegisterRequest(userModel))
            if (!response.isSuccessful) throw Exception("Error creating user: ${response.message()}")
        }
    }

    override suspend fun login(userModel: UserModel): TokenDto {
        return withContext(Dispatchers.IO) {
            val response =
                authApiClient.login(authDtoMapper.fromDomainToLoginRequest(userModel))
            (if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                throw Exception("Error attempting login: ${response.message()}")
            })!!
        }
    }
}