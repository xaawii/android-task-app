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
import androidx.compose.material3.Button
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
import com.example.taskapp.task.presentation.state.AddTaskUIState
import com.example.taskapp.task.presentation.viewmodel.AddTaskViewModel


@Composable
fun AddTaskScreen(navigationController: NavHostController, addTaskViewModel: AddTaskViewModel) {

    LaunchedEffect(true) {
        addTaskViewModel.getForm()
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

            Column(Modifier.fillMaxSize()) {
                Text(text = (uiState as AddTaskUIState.Success).message)
                Button(onClick = { navigationController.popBackStack() }) {
                    Text(text = "Go Back")
                }
            }


        }

        is AddTaskUIState.Editing -> {
            MainBody(
                (uiState as AddTaskUIState.Editing),
                addTaskViewModel
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun MainBody(
    uiState: AddTaskUIState.Editing,
    addTaskViewModel: AddTaskViewModel,
) {

    val datePickerState = rememberDatePickerState()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        addTaskViewModel.onDueDateChanged(
            datePickerState.selectedDateMillis?.let {
                addTaskViewModel.formatMillisToDateString(it)
            } ?: ""
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = uiState.title,
            onValueChange = { addTaskViewModel.onTitleChanged(it) },
            label = { Text("Title") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = uiState.description,
            onValueChange = { addTaskViewModel.onDescriptionChanged(it) },
            label = { Text("Description") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Selector de fecha
        DatePickerDocked(datePickerState, uiState.dueDate)
        Spacer(modifier = Modifier.height(16.dp))

        // Spinner
        DropdownMenu(
            expanded = false,
            onDismissRequest = {}
        ) {
            TaskStatus.entries.forEach { taskStatus ->
                DropdownMenuItem(
                    onClick = {
                        addTaskViewModel.onTaskStatusChanged(taskStatus)
                    },
                    text = { Text(taskStatus.name) }
                )
            }
        }
        TextButton(onClick = { addTaskViewModel.createTask() }) {
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
