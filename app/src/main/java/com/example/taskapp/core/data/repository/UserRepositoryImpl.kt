package com.example.taskapp.core.data.repository

import com.example.taskapp.core.data.remote.api.UserApiClient
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.repository.UserRepository
import com.example.taskapp.core.mappers.UserDtoMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApiClient: UserApiClient,
    private val userDtoMapper: UserDtoMapper
) : UserRepository {
    override suspend fun getUserInfo(userId: Int): UserModel {
        return withContext(Dispatchers.IO) {
            val response = userApiClient.getUserInfo(userId)
            if (response.isSuccessful && response.body() != null) {
                userDtoMapper.fromResponseToDomain(response.body()!!)
            } else {
                throw Exception("Error getting user info: ${response.message()}")
            }
        }
    }
}