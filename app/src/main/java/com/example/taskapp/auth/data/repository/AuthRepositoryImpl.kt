package com.example.taskapp.auth.data.repository

import com.example.taskapp.auth.data.remote.api.AuthApiClient
import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.auth.mappers.AuthDtoMapper
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiClient: AuthApiClient,
    private val authDtoMapper: AuthDtoMapper
) : AuthRepository {
    override suspend fun register(userModel: UserModel): Result<Unit, DataError.Network> {
        return withContext(Dispatchers.IO) {
            try {

                val response =
                    authApiClient.register(authDtoMapper.fromDomainToRegisterRequest(userModel))

                if (response.isSuccessful) {
                    Result.Success(Unit)
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

    override suspend fun login(userModel: UserModel): Result<TokenDto, DataError.Network> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    authApiClient.login(authDtoMapper.fromDomainToLoginRequest(userModel))

                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    println(
                        "Error: ${response.message()}, ${response.code()}, ${
                            response.errorBody()?.string()
                        }"
                    )
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