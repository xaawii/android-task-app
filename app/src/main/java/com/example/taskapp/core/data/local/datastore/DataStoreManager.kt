package com.example.taskapp.core.data.local.datastore

interface DataStoreManager {

    suspend fun saveToken(value: String)
    suspend fun getToken(): String?

    suspend fun saveUserId(value: Int)
    suspend fun getUserId(): Int?

    suspend fun saveUserEmail(value: String)
    suspend fun getUserEmail(): String?


}