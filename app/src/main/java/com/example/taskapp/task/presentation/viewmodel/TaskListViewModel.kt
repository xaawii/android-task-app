package com.example.taskapp.task.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<TaskListUIState>(TaskListUIState.Loading)
    val uiState: StateFlow<TaskListUIState> = _uiState

    private var taskList: MutableList<TaskUIModel> = mutableListOf()


    //event for task deleted
    private val _taskDeletedEvent = MutableSharedFlow<Boolean>()
    val taskDeletedEvent: SharedFlow<Boolean> = _taskDeletedEvent


    fun getTasks() {
        viewModelScope.launch {
            try {
                val tasks = taskUIModelMapper.fromDomainListToUIList(getAllTasksByUserIdUseCase(1))
                taskList = tasks.toMutableList();
                _uiState.value = TaskListUIState.Success(taskList.toList())
            } catch (e: Exception) {
                _uiState.value = TaskListUIState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun onItemRemove(taskUIModel: TaskUIModel) {
        viewModelScope.launch {
            try {

                deleteTaskByIdUseCase(taskUIModel.id)

                taskList.removeIf { it.id == taskUIModel.id }

                _uiState.value = TaskListUIState.Success(taskList.toList())

                _taskDeletedEvent.emit(true)
            } catch (e: Exception) {
                _taskDeletedEvent.emit(false)
            }
        }
    }
}