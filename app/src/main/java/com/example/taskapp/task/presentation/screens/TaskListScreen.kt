package com.example.taskapp.task.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.taskapp.task.presentation.components.LoadingComponent
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.TaskListUIState
import com.example.taskapp.task.presentation.viewmodel.TaskListViewModel
import kotlinx.coroutines.delay

@Composable
fun TaskScreen(taskListViewModel: TaskListViewModel) {

    LaunchedEffect(true) {
        taskListViewModel.getTasks()
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<TaskListUIState>(
        initialValue = TaskListUIState.Loading,
        key1 = lifecycle,
        key2 = taskListViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            taskListViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is TaskListUIState.Error -> {
            Text("Error: ${(uiState as TaskListUIState.Error).message}")
        }

        TaskListUIState.Loading -> {
            LoadingComponent()
        }

        is TaskListUIState.Success -> {
            MainBody(uiState, taskListViewModel)
        }
    }


}

@Composable
private fun MainBody(
    uiState: TaskListUIState,
    taskListViewModel: TaskListViewModel
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Blue)
    ) {
        WelcomeUserHeader()
        CardTaskBody(uiState, taskListViewModel)

    }


}

@Composable
private fun CardTaskBody(
    uiState: TaskListUIState,
    taskListViewModel: TaskListViewModel
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Tareas",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            TasksList((uiState as TaskListUIState.Success).tasks, taskListViewModel)
        }

    }
}

@Composable
private fun WelcomeUserHeader() {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 38.dp), horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Hola,", style = TextStyle(fontSize = 16.sp, color = Color.White))
        Text(
            text = "Xavi",
            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        )
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TasksList(tasks: List<TaskUIModel>, taskListViewModel: TaskListViewModel) {

    LazyColumn {
        items(tasks, key = { it.id }) {
            ItemTask(
                Modifier
                    .animateItemPlacement(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                taskModel = it, taskListViewModel = taskListViewModel
            )
        }
    }
}


@Composable
fun ItemTask(
    modifier: Modifier,
    taskModel: TaskUIModel,
    taskListViewModel: TaskListViewModel
) {
    SwipeToDeleteContainer(
        item = taskModel,
        onDelete = { taskListViewModel.onItemRemove(taskModel) }) {
        TaskCard(modifier = modifier, taskModel = taskModel)
    }
}

@Composable
private fun TaskCard(
    modifier: Modifier,
    taskModel: TaskUIModel
) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.Green),
            )
            Text(
                text = taskModel.title, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}


//yt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = {
                DeleteBackground(swipeDismissState = state)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

