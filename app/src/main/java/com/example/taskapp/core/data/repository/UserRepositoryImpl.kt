package com.example.taskapp.core.data.repository

import com.example.taskapp.core.data.remote.api.UserApiClient
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.repository.UserRepository
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.mappers.UserDtoMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiClient: UserApiClient,
    private val userDtoMapper: UserDtoMapper
) : UserRepository {
    override suspend fun getUserInfoById(userId: Int): Result<UserModel, DataError.Network> {
        return withContext(Dispatchers.IO) {

            try {
                val response = userApiClient.getUserInfoById(userId)

                if (response.isSuccessful && response.body() != null) {
                    Result.Success(userDtoMapper.fromResponseToDomain(response.body()!!))
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

    override suspend fun getUserInfoByEmail(userEmail: String): Result<UserModel, DataError.Network> {
        return withContext(Dispatchers.IO) {

            try {
                println("LLEGA AL USER REPO")
                val response = userApiClient.getUserInfoByEmail(userEmail)
                println(
                    "ERROR?? ${response.code()} ${response.message()} ${
                        response.errorBody()?.string()
                    }"
                )
                if (response.isSuccessful) {

                    Result.Success(userDtoMapper.fromResponseToDomain(response.body()!!))
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