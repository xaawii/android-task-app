package com.example.taskapp.core.domain.usecases

import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Int): UserModel {
        return userRepository.getUserInfo(userId)
    }
}