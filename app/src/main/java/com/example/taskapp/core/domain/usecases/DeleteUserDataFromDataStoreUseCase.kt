package com.example.taskapp.core.domain.usecases

import com.example.taskapp.core.data.local.datastore.DataStoreManager
import javax.inject.Inject

class DeleteUserDataFromDataStoreUseCase @Inject constructor(private val dataStoreManager: DataStoreManager){

    suspend operator fun invoke(){
        dataStoreManager.saveToken("")
        dataStoreManager.saveUserEmail("")
        dataStoreManager.saveUserId(0)
    }
}