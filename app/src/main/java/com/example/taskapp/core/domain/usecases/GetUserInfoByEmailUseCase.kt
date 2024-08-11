package com.example.taskapp.core.domain.usecases

import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.repository.UserRepository
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import javax.inject.Inject

class GetUserInfoByEmailUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(userEmail: String): Result<UserModel, DataError.Network> {
        return userRepository.getUserInfoByEmail(userEmail)
    }
}