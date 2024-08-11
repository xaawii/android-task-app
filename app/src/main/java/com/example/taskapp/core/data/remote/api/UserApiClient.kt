package com.example.taskapp.core.data.remote.api

import com.example.taskapp.core.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiClient {

    @GET("/users/{userId}")
    suspend fun getUserInfoById(
        @Path("userId") userId: Int
    ): Response<UserResponse>

    @GET("/users/email/{userEmail}")
    suspend fun getUserInfoByEmail(
        @Path("userEmail") userEmail: String
    ): Response<UserResponse>

}