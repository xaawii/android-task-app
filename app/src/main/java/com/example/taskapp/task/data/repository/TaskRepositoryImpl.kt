package com.example.taskapp.task.data.repository

import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.task.data.remote.api.TaskApiClient
import com.example.taskapp.task.domain.models.TaskModel
import com.example.taskapp.task.domain.repository.TaskRepository
import com.example.taskapp.task.mappers.TaskDtoMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val apiClient: TaskApiClient,
    private val taskDtoMapper: TaskDtoMapper
) : TaskRepository {
    override suspend fun getAllTasksByUserId(userId: Int): Result<List<TaskModel>, DataError.Network> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiClient.getAllTasks(userId)
                if (response.isSuccessful) {
                    val body = response.body() ?: emptyList()

                    Result.Success(taskDtoMapper.fromResponseListToDomainList(body))

                } else {
                    when (response.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }

            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: Exception) {
                Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun getTaskById(taskId: Long): Result<TaskModel, DataError.Network> {

        return withContext(Dispatchers.IO) {
            try {
                val response = apiClient.getTaskById(taskId)
                if (response.isSuccessful && response.body() != null) {
                    Result.Success(taskDtoMapper.fromResponseToDomain(response.body()!!))
                } else {
                    when (response.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }

            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: Exception) {
                Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun deleteTask(taskId: Long): Result<Unit, DataError.Network> {

        return withContext(Dispatchers.IO) {
            try {
                val response = apiClient.deleteTaskById(taskId)
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    when (response.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        404 -> Result.Error(DataError.Network.NOT_FOUND)
                        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: Exception) {
                Result.Error(DataError.Network.UNKNOWN)
            }

        }

    }

    override suspend fun deleteTaskBatch(ids: List<Long>): Result<Unit, DataError.Network> {

        return withContext(Dispatchers.IO) {
            try {
                val response = apiClient.deleteTasksByIdInBatch(ids = ids)
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    when (response.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        404 -> Result.Error(DataError.Network.NOT_FOUND)
                        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: Exception) {
                Result.Error(DataError.Network.UNKNOWN)
            }
        }
    }

    override suspend fun createTask(
        taskModel: TaskModel,
        userId: Int
    ): Result<Unit, DataError.Network> {

        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiClient.createTask(userId, taskDtoMapper.fromDomainToRequest(taskModel))
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    when (response.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        404 -> Result.Error(DataError.Network.NOT_FOUND)
                        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: Exception) {
                Result.Error(DataError.Network.UNKNOWN)
            }
        }


    }

    override suspend fun updateTask(taskModel: TaskModel): Result<Unit, DataError.Network> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiClient.updateTask(taskModel.id, taskDtoMapper.fromDomainToRequest(taskModel))
                if (response.isSuccessful) {
                    Result.Success(Unit)
                } else {
                    when (response.code()) {
                        400 -> Result.Error(DataError.Network.BAD_REQUEST)
                        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                        403 -> Result.Error(DataError.Network.FORBIDDEN)
                        404 -> Result.Error(DataError.Network.NOT_FOUND)
                        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
                        else -> Result.Error(DataError.Network.UNKNOWN)
                    }
                }
            } catch (e: IOException) {
                Result.Error(DataError.Network.NO_INTERNET)
            } catch (e: Exception) {
                Result.Error(DataError.Network.UNKNOWN)
            }
        }

    }
}