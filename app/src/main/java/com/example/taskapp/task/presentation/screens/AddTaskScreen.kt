package com.example.taskapp.task.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
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
import com.example.taskapp.core.presentation.components.ErrorComponent
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.MyDatePicker
import com.example.taskapp.core.presentation.components.MyFormTextField
import com.example.taskapp.core.presentation.components.MyTimePicker
import com.example.taskapp.core.presentation.components.SuccessComponent
import com.example.taskapp.core.presentation.components.TopAppBarBack
import com.example.taskapp.task.presentation.state.AddTaskUIState
import com.example.taskapp.task.presentation.viewmodel.AddTaskViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@Composable
fun AddTaskScreen(
    navigationController: NavHostController, addTaskViewModel: AddTaskViewModel, taskId: Long = 0L
) {

    val context = LocalContext.current

    LaunchedEffect(true) {
        if (taskId == 0L) addTaskViewModel.getEmptyForm() else addTaskViewModel.getTaskForm(taskId)
    }

    LaunchedEffect(key1 = addTaskViewModel.errorEvent) {
        addTaskViewModel.errorEvent.collectLatest {
            Toast.makeText(context, it.asString(context), Toast.LENGTH_SHORT).show()
        }
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
            ErrorComponent(
                text = stringResource(R.string.an_error_has_occurred),
                reload = { addTaskViewModel.getTaskForm(taskId) },
                hasBackButton = true,
                onBackPressed = navigationController::popBackStack
            )
        }

        AddTaskUIState.Loading -> {
            LoadingComponent()
        }

        is AddTaskUIState.Editing -> {
            MainBody(
                (uiState as AddTaskUIState.Editing), addTaskViewModel, navigationController
            )
        }

        AddTaskUIState.Created -> {
            SuccessComponent(text = stringResource(R.string.task_created), onFinish = navigationController::popBackStack)
        }

        AddTaskUIState.Updated -> {
            SuccessComponent(text = stringResource(R.string.task_updated), onFinish = navigationController::popBackStack)
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
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            modifier = Modifier.height(150.dp),
            label = stringResource(R.string.description),
            value = uiState.description,
            singleLine = false,
            maxLines = 20,
            keyboardType = KeyboardType.Text,
            onValueChange = addTaskViewModel::onDescriptionChanged
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Selector de fecha
        MyDatePicker(uiState.dueDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))) { datePickerState ->
            datePickerState.selectedDateMillis?.let {
                addTaskViewModel.onDueDateChanged(
                    LocalDate.ofEpochDay(it)
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))

        //Selector hora
        MyTimePicker(
            selectedTime = uiState.dueTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        ) { addTaskViewModel.onDueTimeChanged(LocalTime.of(it.hour, it.minute)) }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = addTaskViewModel::onButtonPressed,
            enabled = uiState.formIsValid
        ) {
            val text = if (uiState.mode == "update") {
                stringResource(R.string.update)
            } else stringResource(
                R.string.create
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}













