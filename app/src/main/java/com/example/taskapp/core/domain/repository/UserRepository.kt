package com.example.taskapp.core.domain.repository

import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result

interface UserRepository {

    suspend fun getUserInfoById(userId: Int): Result<UserModel, DataError.Network>
    suspend fun getUserInfoByEmail(userEmail: String): Result<UserModel, DataError.Network>
}