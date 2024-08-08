package com.example.taskapp.auth.domain.usecases

import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.core.data.local.datastore.DataStoreManager
import com.example.taskapp.core.domain.model.UserModel
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreManager: DataStoreManager
) {

    suspend operator fun invoke(userModel: UserModel) {
        val token = authRepository.login(userModel)
        dataStoreManager.saveToken(token.token)
        dataStoreManager.saveUserEmail(userModel.email)
    }
}