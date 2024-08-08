package com.example.taskapp.auth.mappers

import com.example.taskapp.auth.data.remote.dto.LoginRequest
import com.example.taskapp.auth.data.remote.dto.RegisterRequest
import com.example.taskapp.core.domain.model.UserModel
import javax.inject.Inject

class AuthDtoMapper @Inject constructor() {

    fun fromDomainToRegisterRequest(userModel: UserModel): RegisterRequest {
        return RegisterRequest(
            email = userModel.email,
            name = userModel.name,
            password = userModel.password
        )
    }

    fun fromDomainToLoginRequest(userModel: UserModel): LoginRequest {
        return LoginRequest(
            email = userModel.email,
            password = userModel.password
        )
    }
}