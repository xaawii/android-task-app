package com.example.taskapp.core.mappers

import com.example.taskapp.core.data.remote.dto.UserResponse
import com.example.taskapp.core.domain.model.UserModel
import javax.inject.Inject

class UserDtoMapper @Inject constructor() {

    fun fromResponseToDomain(userResponse: UserResponse): UserModel {
        return UserModel(
            id = userResponse.id,
            name = userResponse.name,
            email = userResponse.email,
            password = userResponse.password,
            role = userResponse.password
        )
    }
}