package com.example.taskapp.task.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.presentation.utils.asUiText
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

            when (val result = getTaskById(taskId)) {
                is Result.Error -> _uiState.value = AddTaskUIState.Error(result.error.asUiText())
                is Result.Success -> {

                    val task = result.data

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
                }
            }
        }
    }

    fun onButtonPressed() {
        viewModelScope.launch {

            (_uiState.value as? AddTaskUIState.Editing)?.apply {

                val task = TaskUIModel(
                    id,
                    title,
                    description,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    dataConverters.formatDateStringToLocalDateTime(
                        dueDate,
                        dueTime
                    ),
                    taskStatus,
                    1,
                    false
                )

                _uiState.value = AddTaskUIState.Loading

                if (mode == "update") updateTask(task) else createTask(task)

            }

        }

    }

    private suspend fun createTask(task: TaskUIModel) {
        when (val result = createTaskUseCase(taskUIModelMapper.fromUItoDomain(task))) {
            is Result.Error -> _uiState.value =
                AddTaskUIState.Error(result.error.asUiText())

            is Result.Success -> _uiState.value = AddTaskUIState.Success("Task created")
        }
    }

    private suspend fun updateTask(task: TaskUIModel) {
        when (val result = updateTaskUseCase(
            taskUIModelMapper.fromUItoDomain(
                task
            )
        )) {
            is Result.Error -> _uiState.value =
                AddTaskUIState.Error(result.error.asUiText())

            is Result.Success -> _uiState.value = AddTaskUIState.Success("Task updated")
        }
    }

    fun onTitleChanged(title: String) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            _uiState.value = copy(title = title)
        }

        checkFormIsValid()
    }

    fun onDescriptionChanged(description: String) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            _uiState.value = copy(description = description)
        }
    }

    fun onDueDateChanged(dueDate: String) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            _uiState.value = copy(dueDate = dueDate)
        }
    }

    fun onDueTimeChanged(hour: Int, minute: Int) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            _uiState.value = copy(
                dueTime = formatTimeToString(
                    hour,
                    minute
                )
            )
        }
    }

    fun onTaskStatusChanged(taskStatus: TaskStatus) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            _uiState.value = copy(taskStatus = taskStatus)
        }
    }

    fun formatMillisToDateString(millis: Long): String {
        return dataConverters.convertMillisToDateString(millis)
    }

    private fun formatTimeToString(hour: Int, minute: Int): String {
        return dataConverters.formatTimeToString(hour, minute)
    }

    private fun checkFormIsValid() {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            val formValid = title.isNotBlank()
            _uiState.value = copy(formIsValid = formValid)
        }
    }

    fun resetState() {
        _uiState.value = AddTaskUIState.Editing()
    }


}

