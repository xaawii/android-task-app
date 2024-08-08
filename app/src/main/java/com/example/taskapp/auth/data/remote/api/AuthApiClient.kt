package com.example.taskapp.auth.data.remote.api

import com.example.taskapp.auth.data.remote.dto.LoginRequest
import com.example.taskapp.auth.data.remote.dto.RegisterRequest
import com.example.taskapp.auth.data.remote.dto.TokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiClient {

    @POST("/auth/create")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<Void>

    @POST("/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<TokenDto>

}