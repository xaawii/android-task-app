package com.example.taskapp.task.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.presentation.components.LoadingComponent
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.AddTaskUIState
import com.example.taskapp.task.presentation.viewmodel.AddTaskViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AddTaskScreen(navigationController: NavHostController, addTaskViewModel: AddTaskViewModel) {

    LaunchedEffect(true){
        addTaskViewModel.getTask()
    }


    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<AddTaskUIState>(
        initialValue = AddTaskUIState.Loading,
        key1 = lifecycle,
        key2 = addTaskViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            addTaskViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is AddTaskUIState.Error -> {
            Text("Error: ${(uiState as AddTaskUIState.Error).message}")
        }

        AddTaskUIState.Loading -> {
            LoadingComponent()
        }

        is AddTaskUIState.Success -> {
            MainBody(
                (uiState as AddTaskUIState.Success).task,
                addTaskViewModel,
                navigationController
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainBody(
    task: TaskUIModel,
    addTaskViewModel: AddTaskViewModel,
    navigationController: NavHostController
) {
    var textTile by rememberSaveable { mutableStateOf("") }
    var textDescription by rememberSaveable { mutableStateOf("") }
    var selectedStatus by rememberSaveable { mutableStateOf(TaskStatus.PENDING) }

    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = textTile,
            onValueChange = { textTile = it },
            label = { Text("Title") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = textDescription,
            onValueChange = { textDescription = it },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Selector de fecha
        DatePickerDocked(datePickerState, selectedDate)
        Spacer(modifier = Modifier.height(16.dp))

        // Spinner
        DropdownMenu(
            expanded = false,
            onDismissRequest = {}
        ) {
            TaskStatus.entries.forEach { taskStatus ->
                DropdownMenuItem(
                    onClick = {
                        selectedStatus = taskStatus
                    },
                    text = { Text(taskStatus.name) }
                )
            }
        }
        TextButton(onClick = { addTaskViewModel.createTask(textTile, textDescription, selectedDate) }) {
            Text("Crear")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    datePickerState: DatePickerState,
    selectedDate: String
) {
    var showDatePicker by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Pick a date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
