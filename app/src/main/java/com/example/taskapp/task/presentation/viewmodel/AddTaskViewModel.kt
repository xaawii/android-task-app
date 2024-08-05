package com.example.taskapp.task.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.domain.usecases.CreateTaskUseCase
import com.example.taskapp.task.mappers.TaskUIModelMapper
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.AddTaskUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val taskUIModelMapper: TaskUIModelMapper,
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

    private var _uiState = MutableStateFlow<AddTaskUIState>(AddTaskUIState.Loading)
    val uiState: StateFlow<AddTaskUIState> = _uiState

    fun getTask() {
        viewModelScope.launch {

            val task = TaskUIModel(
                0,
                "",
                "",
                "",
                "",
                "",
                TaskStatus.PENDING,
                1,
                false
            )

            _uiState.value = AddTaskUIState.Success(task)

        }
    }

    fun createTask(title: String, description: String, date: String) {
        viewModelScope.launch {

            val task = TaskUIModel(
                0,
                title,
                description,
                "",
                "",
                formatDateForRequest(date),
                TaskStatus.PENDING,
                1,
                false
            )

            try {
                createTaskUseCase(taskUIModelMapper.fromUItoDomain(task))

            } catch (e: Exception) {
                _uiState.value = AddTaskUIState.Error(e.message ?: "Unknown Error")
            }

        }


    }


}

fun formatDateForRequest(date: String): String {
    val originalFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val originalLocalDate = LocalDate.parse(date, originalFormatter)
    val newFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val newLocalDateTime = LocalDateTime.of(originalLocalDate, LocalTime.of(15, 30))
    return newLocalDateTime.format(newFormatter)
}