package com.example.taskapp.auth.domain.usecases

import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.core.domain.model.UserModel
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(userModel: UserModel) {
        authRepository.register(userModel)
    }
}