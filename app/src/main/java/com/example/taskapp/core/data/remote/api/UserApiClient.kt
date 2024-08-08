package com.example.taskapp.core.data.remote.api

import com.example.taskapp.core.data.remote.dto.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApiClient {

    @GET("/users/{userId}")
    suspend fun getUserInfo(
        @Path("userId") userId: Int
    ): Response<UserResponse>

}