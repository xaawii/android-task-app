package com.example.taskapp.auth.data.repository

import com.example.taskapp.auth.data.exceptions.EmailAlreadyInUseException
import com.example.taskapp.auth.data.remote.api.AuthApiClient
import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.auth.mappers.AuthDtoMapper
import com.example.taskapp.core.domain.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiClient: AuthApiClient,
    private val authDtoMapper: AuthDtoMapper
) : AuthRepository {
    override suspend fun register(userModel: UserModel) {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    authApiClient.register(authDtoMapper.fromDomainToRegisterRequest(userModel))
                handleResponse(response)
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
                throw Exception("HTTP error: $errorMessage")
            } catch (e: IOException) {
                throw Exception("Network error: ${e.message}")
            } catch (e: Exception) {
                throw Exception("Unknown error: ${e.message}")
            }
        }
    }

    override suspend fun login(userModel: UserModel): TokenDto {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    authApiClient.login(authDtoMapper.fromDomainToLoginRequest(userModel))
                handleResponse(response)
                    ?: throw Exception("Error attempting login: No response body found")
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
                throw Exception("HTTP error: $errorMessage")
            } catch (e: IOException) {
                throw Exception("Network error: ${e.message}")
            } catch (e: Exception) {
                throw Exception("Unknown error: ${e.message}")
            }
        }
    }

    private fun <T> handleResponse(response: Response<T>): T? {
        if (response.isSuccessful) {
            return response.body()
        } else {
            val errorMessage = response.errorBody()?.string()
            if (errorMessage?.contains("already in use") == true) {
                throw EmailAlreadyInUseException()
            } else {
                throw Exception("Error: $errorMessage")
            }

        }
    }
}