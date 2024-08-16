package com.example.taskapp.task.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.R
import com.example.taskapp.core.presentation.components.CircleBackground
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.MyDatePicker
import com.example.taskapp.core.presentation.components.MyDropDownMenu
import com.example.taskapp.core.presentation.components.MyFormTextField
import com.example.taskapp.core.presentation.components.MyTimePicker
import com.example.taskapp.core.presentation.components.TopAppBarBack
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.presentation.state.AddTaskUIState
import com.example.taskapp.task.presentation.viewmodel.AddTaskViewModel


@Composable
fun AddTaskScreen(
    navigationController: NavHostController, addTaskViewModel: AddTaskViewModel, taskId: Long = 0L
) {

    val context = LocalContext.current

    LaunchedEffect(true) {
        if (taskId == 0L) addTaskViewModel.getEmptyForm() else addTaskViewModel.getTaskForm(taskId)
    }


    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<AddTaskUIState>(
        initialValue = AddTaskUIState.Loading, key1 = lifecycle, key2 = addTaskViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            addTaskViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is AddTaskUIState.Error -> {
            Text("Error: ${(uiState as AddTaskUIState.Error).message.asString(context)}")
        }

        AddTaskUIState.Loading -> {
            LoadingComponent()
        }

        is AddTaskUIState.Success -> {

            SuccessView(uiState, navigationController)

        }

        is AddTaskUIState.Editing -> {
            MainBody(
                (uiState as AddTaskUIState.Editing), addTaskViewModel, navigationController
            )
        }
    }
}

@Composable
private fun SuccessView(
    uiState: AddTaskUIState, navigationController: NavHostController
) {
    Column(Modifier.fillMaxSize()) {
        Text(text = (uiState as AddTaskUIState.Success).message)
        Button(onClick = { navigationController.popBackStack() }) {
            Text(text = "Go Back")
        }
    }
}

@Composable
private fun MainBody(
    uiState: AddTaskUIState.Editing,
    addTaskViewModel: AddTaskViewModel,
    navigationController: NavHostController
) {

    val title =
        if (uiState.mode != "update") stringResource(R.string.create_task) else stringResource(R.string.update_task)

    CircleBackground(color = MaterialTheme.colorScheme.primary) {
        Scaffold(containerColor = Color.Transparent, topBar = {
            TopAppBarBack(
                title = title, onBackPressed = navigationController::popBackStack
            )
        }) { contentPadding ->
            FormBody(contentPadding, uiState, addTaskViewModel)
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormBody(
    paddingValues: PaddingValues,
    uiState: AddTaskUIState.Editing,
    addTaskViewModel: AddTaskViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        //title
        MyFormTextField(
            label = stringResource(R.string.title),
            value = uiState.title,
            keyboardType = KeyboardType.Text,
            onValueChange = addTaskViewModel::onTitleChanged
        )
        Spacer(modifier = Modifier.height(16.dp))

        //description
        MyFormTextField(
            modifier = Modifier.height(50.dp),
            label = "Description",
            value = uiState.description,
            keyboardType = KeyboardType.Text,
            onValueChange = addTaskViewModel::onDescriptionChanged
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Selector de fecha
        MyDatePicker(uiState.dueDate) { datePickerState ->
            addTaskViewModel.onDueDateChanged(datePickerState.selectedDateMillis?.let {
                addTaskViewModel.formatMillisToDateString(it)
            } ?: "")
        }
        Spacer(modifier = Modifier.height(16.dp))

        //Selector hora
        MyTimePicker(
            selectedTime = uiState.dueTime
        ) { addTaskViewModel.onDueTimeChanged(it.hour, it.minute) }
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.mode == "update") {
            // Spinner
            MyDropDownMenu(
                items = TaskStatus.entries,
                selectedItem = uiState.taskStatus.name,
                onSelected = addTaskViewModel::onTaskStatusChanged,
                label = "Select task status"
            ) {
                it.name
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        TextButton(onClick = addTaskViewModel::onButtonPressed, enabled = uiState.formIsValid) {
            Text("Create")
        }
    }
}













