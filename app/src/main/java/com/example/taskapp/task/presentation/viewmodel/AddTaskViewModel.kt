package com.example.taskapp.task.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.core.utils.DataConverters
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.domain.usecases.CreateTaskUseCase
import com.example.taskapp.task.domain.usecases.GetTaskById
import com.example.taskapp.task.domain.usecases.UpdateTaskUseCase
import com.example.taskapp.task.mappers.TaskUIModelMapper
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.AddTaskUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskUIModelMapper: TaskUIModelMapper,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskById: GetTaskById,
    private val dataConverters: DataConverters
) : ViewModel() {

    private var _uiState = MutableStateFlow<AddTaskUIState>(AddTaskUIState.Loading)
    val uiState: StateFlow<AddTaskUIState> = _uiState

    fun getEmptyForm() {
        viewModelScope.launch {

            _uiState.value = AddTaskUIState.Editing()

        }
    }

    fun getTaskForm(taskId: Long) {
        viewModelScope.launch {

            try {
                val task = getTaskById(taskId)

                _uiState.value =
                    AddTaskUIState.Editing(
                        id = taskId,
                        title = task.title,
                        description = task.description,
                        dueDate = dataConverters.formatLocalDateTimeToDateTimeString(task.dueDate),
                        dueTime = dataConverters.formatTimeToString(
                            task.dueDate.hour,
                            task.dueDate.minute
                        ),
                        taskStatus = task.status,
                        mode = "update"
                    )

            } catch (e: Exception) {
                _uiState.value = AddTaskUIState.Error(e.message ?: "Unknown Error")
            }

        }
    }

    fun createTask() {
        viewModelScope.launch {

            if (_uiState.value is AddTaskUIState.Editing) {

                val tempUIState = (_uiState.value as AddTaskUIState.Editing)

                val task = TaskUIModel(
                    tempUIState.id,
                    tempUIState.title,
                    tempUIState.description,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    dataConverters.formatDateStringToLocalDateTime(
                        tempUIState.dueDate,
                        tempUIState.dueTime
                    ),
                    tempUIState.taskStatus,
                    1,
                    false
                )

                try {
                    _uiState.value = AddTaskUIState.Loading

                    if (tempUIState.mode == "update") updateTaskUseCase(
                        taskUIModelMapper.fromUItoDomain(
                            task
                        )
                    ) else createTaskUseCase(taskUIModelMapper.fromUItoDomain(task))

                    _uiState.value = AddTaskUIState.Success("Task created")

                } catch (e: Exception) {
                    _uiState.value = AddTaskUIState.Error(e.message ?: "Unknown Error")
                }
            }

        }

    }

    fun onTitleChanged(title: String) {
        if (_uiState.value is AddTaskUIState.Editing) {
            _uiState.value = (_uiState.value as AddTaskUIState.Editing).copy(title = title)
        }
    }

    fun onDescriptionChanged(description: String) {
        if (_uiState.value is AddTaskUIState.Editing) {
            _uiState.value =
                (_uiState.value as AddTaskUIState.Editing).copy(description = description)
        }
    }

    fun onDueDateChanged(dueDate: String) {
        if (_uiState.value is AddTaskUIState.Editing) {
            _uiState.value = (_uiState.value as AddTaskUIState.Editing).copy(dueDate = dueDate)
        }
    }

    fun onDueTimeChanged(hour: Int, minute: Int) {
        if (_uiState.value is AddTaskUIState.Editing) {
            _uiState.value = (_uiState.value as AddTaskUIState.Editing).copy(
                dueTime = formatTimeToString(
                    hour,
                    minute
                )
            )
        }
    }

    fun onTaskStatusChanged(taskStatus: TaskStatus) {
        if (_uiState.value is AddTaskUIState.Editing) {
            _uiState.value =
                (_uiState.value as AddTaskUIState.Editing).copy(taskStatus = taskStatus)
        }
    }

    fun formatMillisToDateString(millis: Long): String {
        return dataConverters.convertMillisToDateString(millis)
    }

    private fun formatTimeToString(hour: Int, minute: Int): String {
        return dataConverters.formatTimeToString(hour, minute)
    }


}

