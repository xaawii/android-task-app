package com.example.taskapp.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.presentation.utils.asUiText
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.domain.usecases.DeleteTaskByIdUseCase
import com.example.taskapp.task.domain.usecases.GetTaskById
import com.example.taskapp.task.domain.usecases.UpdateTaskUseCase
import com.example.taskapp.task.mappers.TaskUIModelMapper
import com.example.taskapp.task.presentation.state.DetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val taskUIModelMapper: TaskUIModelMapper,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val getTaskById: GetTaskById
) : ViewModel() {
    private var _uiState = MutableStateFlow<DetailUIState>(DetailUIState.Loading)
    val uiState: StateFlow<DetailUIState> = _uiState

    //event for task deleted
    private val _taskDeletedEvent = MutableSharedFlow<Boolean>()
    val taskDeletedEvent: SharedFlow<Boolean> = _taskDeletedEvent

    //change status event
    private val _taskStatusEvent = MutableSharedFlow<Boolean>()
    val taskStatusEvent: SharedFlow<Boolean> = _taskStatusEvent


    fun getTask(taskId: Long) {
        viewModelScope.launch {
            when (val result = getTaskById(taskId)) {
                is Result.Error -> _uiState.value = DetailUIState.Error(result.error.asUiText())
                is Result.Success -> _uiState.value =
                    DetailUIState.Success(taskUIModelMapper.fromDomainToUI(result.data))
            }
        }
    }

    fun updateTaskStatus(taskStatus: TaskStatus) {
        viewModelScope.launch {

            (_uiState.value as? DetailUIState.Success)?.apply {

                val task = task.copy(status = taskStatus)

                when (updateTaskUseCase(taskUIModelMapper.fromUItoDomain(task))) {
                    is Result.Error -> _taskStatusEvent.emit(false)

                    is Result.Success -> {
                        _taskStatusEvent.emit(true)
                        _uiState.value = DetailUIState.Success(task = task)
                    }
                }


            }
        }
    }


    fun deleteTask() {
        viewModelScope.launch {
            (_uiState.value as? DetailUIState.Success)?.apply {
                _uiState.value = DetailUIState.Loading
                when (deleteTaskByIdUseCase(task.id)) {
                    is Result.Error -> {
                        _taskDeletedEvent.emit(false)
                        _uiState.value = copy()
                    }
                    is Result.Success -> {
                        _taskDeletedEvent.emit(true)
                        _uiState.value = DetailUIState.Deleted
                    }
                }
            }

        }

    }


}