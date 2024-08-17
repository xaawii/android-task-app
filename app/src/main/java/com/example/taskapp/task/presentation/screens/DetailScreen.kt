package com.example.taskapp.task.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.R
import com.example.taskapp.core.presentation.components.CircleBackground
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.TopAppBarBack
import com.example.taskapp.core.routes.Routes
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.presentation.state.DetailUIState
import com.example.taskapp.task.presentation.state.TaskListUIState
import com.example.taskapp.task.presentation.viewmodel.DetailViewModel
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
            Text("Error: ${(uiState as TaskListUIState.Error).message.asString(context)}")
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
            popUpTo(Routes.DetailScreen) { inclusive = true }
        }
    }


}


@Composable
private fun MainBody(
    uiState: DetailUIState.Success,
    detailViewModel: DetailViewModel,
    navigationController: NavHostController
) {

    CircleBackground(color = MaterialTheme.colorScheme.primary) {
        Scaffold(containerColor = Color.Transparent, topBar = {
            TopAppBarBack(onBackPressed = navigationController::popBackStack)
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


}

@Composable
fun DetailBody(
    uiState: DetailUIState.Success,
    detailViewModel: DetailViewModel
) {

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = uiState.task.title, style = MaterialTheme.typography.titleSmall)
        Text(text = uiState.task.description, style = MaterialTheme.typography.bodyMedium)
        Row {
            Icon(imageVector = Icons.Rounded.CalendarMonth, contentDescription = "date")
            Text(text = uiState.task.dueDate.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
        }
        Row {
            Icon(imageVector = Icons.Rounded.AccessTime, contentDescription = "time")
            Text(text = uiState.task.dueDate.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME))
        }

        Row {
            TaskStatus.entries.forEach {
                StatusView(taskStatus = it) {
                    detailViewModel.updateTaskStatus(it)
                }
            }
        }

    }

}

@Composable
private fun StatusView(taskStatus: TaskStatus, onClick: () -> Unit) {
    Card(Modifier.clickable { onClick() }) {
        Text(text = taskStatus.name)
    }
}
