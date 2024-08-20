package com.example.taskapp.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskapp.auth.domain.usecases.SendNewPasswordAndSecurityCodeUseCase
import com.example.taskapp.auth.domain.usecases.SendRecoverPasswordPetitionUseCase
import com.example.taskapp.auth.domain.usecases.ValidatePasswordSecurityCodeUseCase
import com.example.taskapp.auth.presentation.state.RecoverPasswordUIState
import com.example.taskapp.core.domain.validator.UserDataValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RecoverPasswordViewModel @Inject constructor(
    private val sendRecoverPasswordPetitionUseCase: SendRecoverPasswordPetitionUseCase,
    private val validatePasswordSecurityCodeUseCase: ValidatePasswordSecurityCodeUseCase,
    private val sendNewPasswordAndSecurityCodeUseCase: SendNewPasswordAndSecurityCodeUseCase,
    private val userDataValidator: UserDataValidator
) : ViewModel() {

    private var _uiState =
        MutableStateFlow<RecoverPasswordUIState>(RecoverPasswordUIState.SendEmail())
    val uiState: StateFlow<RecoverPasswordUIState> = _uiState


}