package com.example.taskapp.auth.domain.usecases

import com.example.taskapp.core.data.local.datastore.DataStoreManager
import com.example.taskapp.core.domain.model.UserModel
import javax.inject.Inject

class SaveUserDataUseCase @Inject constructor(private val dataStoreManager: DataStoreManager) {
    suspend operator fun invoke(userModel: UserModel) {
        dataStoreManager.saveUserId(userModel.id)
        dataStoreManager.saveUserEmail(userModel.email)
        dataStoreManager.saveUserName(userModel.name)
    }
}