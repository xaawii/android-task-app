package com.example.taskapp.auth.domain.usecases

import com.example.taskapp.core.data.local.datastore.DataStoreManager
import javax.inject.Inject

class DeleteUserDataFromPreferencesUseCase @Inject constructor(private val dataStoreManager: DataStoreManager) {
    suspend operator fun invoke() {
        dataStoreManager.saveUserEmail("")
        dataStoreManager.saveToken("")
    }
}