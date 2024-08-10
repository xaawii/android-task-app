package com.example.taskapp.auth.domain.usecases

import com.example.taskapp.auth.data.remote.dto.TokenDto
import com.example.taskapp.auth.domain.repository.AuthRepository
import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(userModel: UserModel): Result<TokenDto, DataError.Network> {
        return authRepository.login(userModel)
    }
}