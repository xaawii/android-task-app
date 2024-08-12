package com.example.taskapp.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.core.domain.usecases.DeleteUserDataFromDataStoreUseCase
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.presentation.utils.asUiText
import com.example.taskapp.task.domain.usecases.DeleteTaskByIdUseCase
import com.example.taskapp.task.domain.usecases.GetAllTasksByUserIdUseCase
import com.example.taskapp.task.mappers.TaskUIModelMapper
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.TaskListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskUIModelMapper: TaskUIModelMapper,
    private val getAllTasksByUserIdUseCase: GetAllTasksByUserIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val deleteUserDataFromDataStoreUseCase: DeleteUserDataFromDataStoreUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<TaskListUIState>(TaskListUIState.Loading)
    val uiState: StateFlow<TaskListUIState> = _uiState

    private var taskList: MutableList<TaskUIModel> = mutableListOf()


    //event for task deleted
    private val _taskDeletedEvent = MutableSharedFlow<Boolean>()
    val taskDeletedEvent: SharedFlow<Boolean> = _taskDeletedEvent


    fun getTasks() {
        viewModelScope.launch {

            when (val result = getAllTasksByUserIdUseCase()) {
                is Result.Error -> _uiState.value =
                    TaskListUIState.Error(result.error.asUiText())

                is Result.Success -> {
                    taskList =
                        taskUIModelMapper.fromDomainListToUIList(result.data).toMutableList()
                    _uiState.value = TaskListUIState.Success(taskList.toList())
                }
            }

        }
    }

    fun onItemRemove(taskUIModel: TaskUIModel) {
        viewModelScope.launch {

            when (deleteTaskByIdUseCase(taskUIModel.id)) {
                is Result.Error -> _taskDeletedEvent.emit(false)
                is Result.Success -> {
                    taskList.removeIf { it.id == taskUIModel.id }
                    _uiState.value = TaskListUIState.Success(taskList.toList())
                    _taskDeletedEvent.emit(true)
                }
            }
        }

    }

    fun logOut() {
        viewModelScope.launch {
            _uiState.value = TaskListUIState.LogOut
            deleteUserDataFromDataStoreUseCase()
        }
    }

}