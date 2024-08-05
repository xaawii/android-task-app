package com.example.taskapp.task.data.remote.api

import com.example.taskapp.task.data.remote.dto.TaskRequest
import com.example.taskapp.task.data.remote.dto.TaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @DELETE("/task/batch-delete")
    suspend fun deleteTasksByIdInBatch(
        @Body ids: List<Long>
    ): Response<Void>

    @POST("/task/{userId}")
    suspend fun createTask(
        @Path("userId") userId: Int,
        @Body taskRequest: TaskRequest
    ): Response<TaskResponse>

    @PUT("/task/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body taskRequest: TaskRequest
    ): Response<TaskResponse>

}