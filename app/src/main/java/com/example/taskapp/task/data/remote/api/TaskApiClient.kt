package com.example.taskapp.task.data.remote.api

import com.example.taskapp.task.data.remote.dto.TaskResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskApiClient {

    @GET("/task/user/{userId}")
    suspend fun getAllTasks(
        @Path("userId") userId: Int
    ): Response<List<TaskResponse>>

    @DELETE("/task/{id}")
    suspend fun deleteTaskById(
        @Path("id") id: Long
    ): Response<Void>

}