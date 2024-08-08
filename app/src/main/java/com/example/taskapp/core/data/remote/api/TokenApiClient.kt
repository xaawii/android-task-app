package com.example.taskapp.core.data.remote.api

import com.example.taskapp.auth.data.remote.dto.TokenDto
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface TokenApiClient {

    @POST("/auth/validate/token")
    suspend fun validateToken(
        @Query("token") token: String
    ): Response<TokenDto>

}