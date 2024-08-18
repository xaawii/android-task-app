package com.example.taskapp.task.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.presentation.utils.UiText
import com.example.taskapp.core.presentation.utils.asUiText
import com.example.taskapp.task.domain.usecases.CreateTaskUseCase
import com.example.taskapp.task.domain.usecases.GetTaskById
import com.example.taskapp.task.domain.usecases.UpdateTaskUseCase
import com.example.taskapp.task.mappers.TaskUIModelMapper
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.AddTaskUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskUIModelMapper: TaskUIModelMapper,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTaskById: GetTaskById
) : ViewModel() {

    private var _uiState = MutableStateFlow<AddTaskUIState>(AddTaskUIState.Loading)
    val uiState: StateFlow<AddTaskUIState> = _uiState

    //event for error
    private val _errorEvent = MutableSharedFlow<UiText>()
    val errorEvent: SharedFlow<UiText> = _errorEvent

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
                            dueDate = task.dueDate.toLocalDate(),
                            dueTime = task.dueDate.toLocalTime(),
                            taskStatus = task.status,
                            mode = "update"
                        )

                    checkFormIsValid()
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
                    LocalDateTime.of(dueDate, dueTime),
                    taskStatus,
                    1,
                    false
                )

                if (mode == "update") updateTask(task) else createTask(task)

            }

        }

    }

    private suspend fun createTask(task: TaskUIModel) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {

            _uiState.value = AddTaskUIState.Loading

            when (val result = createTaskUseCase(taskUIModelMapper.fromUItoDomain(task))) {
                is Result.Error -> {
                    _errorEvent.emit(result.error.asUiText())
                    _uiState.value = copy()
                }

                is Result.Success -> _uiState.value = AddTaskUIState.Created
            }
        }

    }

    private suspend fun updateTask(task: TaskUIModel) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {

            _uiState.value = AddTaskUIState.Loading

            when (val result = updateTaskUseCase(
                taskUIModelMapper.fromUItoDomain(
                    task
                )
            )) {
                is Result.Error -> {
                    _errorEvent.emit(result.error.asUiText())
                    _uiState.value = copy()
                }

                is Result.Success -> _uiState.value = AddTaskUIState.Updated
            }
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

    fun onDueDateChanged(localDate: LocalDate) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            _uiState.value = copy(dueDate = localDate)
        }
    }

    fun onDueTimeChanged(localTime: LocalTime) {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            _uiState.value = copy(dueTime = localTime)
        }
    }


    private fun checkFormIsValid() {
        (_uiState.value as? AddTaskUIState.Editing)?.apply {
            val formValid = title.isNotBlank()
            _uiState.value = copy(formIsValid = formValid)
        }
    }


}

