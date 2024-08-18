package com.example.taskapp.task.presentation.viewmodel

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.core.domain.usecases.DeleteUserDataFromDataStoreUseCase
import com.example.taskapp.core.domain.usecases.GetUserNameFromDataStoreUseCase
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.presentation.utils.asUiText
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.domain.usecases.DeleteTaskByIdUseCase
import com.example.taskapp.task.domain.usecases.GetAllTasksByUserIdUseCase
import com.example.taskapp.task.domain.usecases.UpdateTaskUseCase
import com.example.taskapp.task.mappers.TaskUIModelMapper
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.TaskListUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskUIModelMapper: TaskUIModelMapper,
    private val getAllTasksByUserIdUseCase: GetAllTasksByUserIdUseCase,
    private val deleteTaskByIdUseCase: DeleteTaskByIdUseCase,
    private val deleteUserDataFromDataStoreUseCase: DeleteUserDataFromDataStoreUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getUserNameFromDataStoreUseCase: GetUserNameFromDataStoreUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<TaskListUIState>(TaskListUIState.Loading)
    val uiState: StateFlow<TaskListUIState> = _uiState

    private var taskList: MutableList<TaskUIModel> = mutableListOf()


    //event for task deleted
    private val _taskDeletedEvent = MutableSharedFlow<Boolean>()
    val taskDeletedEvent: SharedFlow<Boolean> = _taskDeletedEvent

    //change status event
    private val _taskStatusEvent = MutableSharedFlow<Boolean>()
    val taskStatusEvent: SharedFlow<Boolean> = _taskStatusEvent


    fun getTasks() {
        viewModelScope.launch {

            _uiState.value = TaskListUIState.Loading

            when (val result = getAllTasksByUserIdUseCase()) {
                is Result.Error -> _uiState.value =
                    TaskListUIState.Error(result.error.asUiText())

                is Result.Success -> {
                    taskList =
                        taskUIModelMapper.fromDomainListToUIList(result.data).toMutableList()
                    _uiState.value =
                        TaskListUIState.Success(userName = getUserNameFromDataStoreUseCase())
                    filterTasksBySelectedDay()
                }
            }

        }
    }

    fun onItemRemove(taskUIModel: TaskUIModel) {
        viewModelScope.launch {
            (_uiState.value as? TaskListUIState.Success)?.apply {
                when (deleteTaskByIdUseCase(taskUIModel.id)) {
                    is Result.Error -> _taskDeletedEvent.emit(false)
                    is Result.Success -> {
                        taskList.removeIf { it.id == taskUIModel.id }
                        filterTasksBySelectedDay()
                        _taskDeletedEvent.emit(true)
                    }
                }
            }

        }

    }

    fun changeSelectedDate(newDate: LocalDate) {
        (_uiState.value as? TaskListUIState.Success)?.apply {
            _uiState.value = copy(selectedDate = newDate)
            filterTasksBySelectedDay()
        }


    }

    private fun filterTasksBySelectedDay() {
        (_uiState.value as? TaskListUIState.Success)?.apply {
            _uiState.value = copy(tasks = taskList.filter {
                it.dueDate.toLocalDate() == selectedDate && YearMonth.from(it.dueDate) == yearMonth
            }.toList())
        }
    }

    fun updateTaskStatus(isCompleted: Boolean, taskId: Long) {
        viewModelScope.launch {
            val status = if (isCompleted) TaskStatus.COMPLETED else TaskStatus.PENDING

            val taskIndex = taskList.indexOfFirst { it.id == taskId }
            if (taskIndex != -1) {
                val updatedTask = taskList[taskIndex].copy(status = status)
                taskList[taskIndex] = updatedTask

                when (
                    updateTaskUseCase(taskUIModelMapper.fromUItoDomain(updatedTask))) {
                    is Result.Error -> _taskStatusEvent.emit(false)

                    is Result.Success -> {
                        _taskStatusEvent.emit(true)
                        filterTasksBySelectedDay()
                    }

                }
            }
        }
    }


    fun calculateScrollOffset(listState: LazyListState): Int {
        val layoutInfo = listState.layoutInfo
        val visibleItemsInfo = layoutInfo.visibleItemsInfo
        val selectedItemInfo = visibleItemsInfo.find { it.index == listState.firstVisibleItemIndex }

        return if (selectedItemInfo != null) {
            val parentCenter = layoutInfo.viewportEndOffset / 2
            val itemCenter = selectedItemInfo.offset + (selectedItemInfo.size / 2)
            parentCenter - itemCenter
        } else {
            0
        }
    }

    fun generateDaysInMonth(yearMonth: YearMonth): List<LocalDate> {
        return (1..yearMonth.lengthOfMonth()).map { day ->
            yearMonth.atDay(day)
        }
    }

    fun changeMonth(isNext: Boolean) {
        (_uiState.value as? TaskListUIState.Success)?.apply {
            val resultMonth = if (isNext) yearMonth.plusMonths(1) else yearMonth.minusMonths(1)
            _uiState.value = copy(yearMonth = resultMonth)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            _uiState.value = TaskListUIState.LogOut
            deleteUserDataFromDataStoreUseCase()
        }
    }

}