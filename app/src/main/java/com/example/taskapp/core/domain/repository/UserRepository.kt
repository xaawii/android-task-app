package com.example.taskapp.core.domain.repository

import com.example.taskapp.core.domain.model.UserModel

interface UserRepository {

    suspend fun getUserInfo(userId: Int): UserModel
}