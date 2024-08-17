package com.example.taskapp.task.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.R
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.routes.Routes
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.presentation.state.DetailUIState
import com.example.taskapp.task.presentation.viewmodel.DetailViewModel
import com.example.taskapp.ui.theme.Greyed
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel,
    navigationController: NavHostController,
    taskId: Long
) {

    val context = LocalContext.current

    LaunchedEffect(true) {
        detailViewModel.getTask(taskId)
    }


    LaunchedEffect(key1 = detailViewModel.taskDeletedEvent) {
        detailViewModel.taskDeletedEvent.collectLatest {
            if (it) {
                Toast.makeText(
                    context,
                    context.getString(R.string.task_deleted),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.failed_to_delete_task), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(key1 = detailViewModel.taskStatusEvent) {
        detailViewModel.taskStatusEvent.collectLatest {
            if (it) {
                Toast.makeText(
                    context,
                    context.getString(R.string.task_status_updated), Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.failed_to_update_tasks_status), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<DetailUIState>(
        initialValue = DetailUIState.Loading,
        key1 = lifecycle,
        key2 = detailViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            detailViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is DetailUIState.Error -> {
            Text("Error: ${(uiState as DetailUIState.Error).message.asString(context)}")
        }

        DetailUIState.Loading -> {
            LoadingComponent()
        }

        is DetailUIState.Success -> {
            MainBody(
                (uiState as DetailUIState.Success),
                detailViewModel,
                navigationController
            )
        }

        DetailUIState.Deleted -> navigationController.navigate(Routes.TasksListScreen) {
            popUpTo(Routes.DetailScreen(id = taskId)) { inclusive = true }
        }
    }

}


@Composable
private fun MainBody(
    uiState: DetailUIState.Success,
    detailViewModel: DetailViewModel,
    navigationController: NavHostController
) {


    Scaffold(containerColor = Color.Transparent, topBar = {
        TopAppBarDetails(
            onUpdatePressed = { navigationController.navigate(Routes.AddTaskScreen(id = uiState.task.id)) },
            onDeletePressed = detailViewModel::deleteTask,
            onBackPressed = navigationController::popBackStack
        )
    }) { contentPadding ->

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            item { DetailBody(uiState, detailViewModel) }
        }


    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDetails(
    title: String = "",
    onBackPressed: () -> Unit,
    onUpdatePressed: () -> Unit,
    onDeletePressed: () -> Unit
) {

    var showAlert by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(text = title, style = MaterialTheme.typography.titleSmall) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Go back"
                )
            }
        },
        actions = {
            IconButton(onClick = onUpdatePressed) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = "Update")
            }
            IconButton(onClick = { showAlert = true }) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = "Delete")
            }
        }
    )

    if (showAlert) {
        DeleteAlertDialog(onDismiss = { showAlert = false }, onConfirm = onDeletePressed)
    }

}

@Composable
fun DetailBody(
    uiState: DetailUIState.Success,
    detailViewModel: DetailViewModel
) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.task.title,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.task.description,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(36.dp))
        Row {
            Icon(imageVector = Icons.Rounded.CalendarMonth, contentDescription = "date")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = uiState.task.dueDate.toLocalDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Icon(imageVector = Icons.Rounded.AccessTime, contentDescription = "time")
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = uiState.task.dueDate.toLocalTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
            TaskStatus.entries.forEach {
                val color =
                    if (it == uiState.task.status) MaterialTheme.colorScheme.secondary else Greyed
                StatusView(taskStatus = it, color = color) {
                    detailViewModel.updateTaskStatus(it)
                }
            }
        }

    }

}

@Composable
private fun DeleteAlertDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        title = { Text(text = "Delete task") },
        text = { Text(text = "Are you sure you want to delete this task?") },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "No")
            }
        })
}

@Composable
private fun StatusView(taskStatus: TaskStatus, color: Color, onClick: () -> Unit) {
    Card(
        Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = taskStatus.name.replace("_"," "),
            style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
        )
    }
}
