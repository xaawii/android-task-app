package com.example.taskapp.core.mappers

import com.example.taskapp.core.domain.model.UserModel
import com.example.taskapp.core.presentation.model.UserUIModel
import javax.inject.Inject

class UserUIModelMapper @Inject constructor() {

    fun fromUItoDomain(userUIModel: UserUIModel): UserModel {
        return UserModel(
            id = userUIModel.id,
            email = userUIModel.email,
            name = userUIModel.name,
            password = userUIModel.password,
            role = userUIModel.role
        )
    }

    fun fromDomainToUI(userModel: UserModel): UserUIModel {
        return UserUIModel(
            id = userModel.id,
            email = userModel.email,
            name = userModel.name,
            password = userModel.password,
            role = userModel.role
        )
    }
}