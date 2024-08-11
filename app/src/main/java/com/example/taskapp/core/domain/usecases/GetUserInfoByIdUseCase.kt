package com.example.taskapp.core.domain.usecases

import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.repository.UserRepository
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import javax.inject.Inject

class GetUserInfoByIdUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: Int): Result<UserModel, DataError.Network> {
        return userRepository.getUserInfoById(userId)
    }
}