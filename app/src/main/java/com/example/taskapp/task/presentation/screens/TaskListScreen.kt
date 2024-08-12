package com.example.taskapp.task.presentation.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.routes.Routes
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.TaskListUIState
import com.example.taskapp.task.presentation.viewmodel.TaskListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TaskScreen(taskListViewModel: TaskListViewModel, navigationController: NavHostController) {

    val context = LocalContext.current

    LaunchedEffect(true) {
        taskListViewModel.getTasks()
    }


    LaunchedEffect(key1 = taskListViewModel.taskDeletedEvent) {
        taskListViewModel.taskDeletedEvent.collectLatest {
            if (it) {
                Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete task", Toast.LENGTH_SHORT).show()
            }
        }
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
            Text("Error: ${(uiState as TaskListUIState.Error).message.asString(context)}")
        }

        TaskListUIState.Loading -> {
            LoadingComponent()
        }

        is TaskListUIState.Success -> {
            MainBody(
                (uiState as TaskListUIState.Success).tasks,
                taskListViewModel,
                navigationController
            )
        }

        TaskListUIState.LogOut -> {
            navigationController.navigate(Routes.LoginScreen) {
                popUpTo(Routes.TasksListScreen) { inclusive = true }
            }
        }
    }


}

@Composable
private fun MainBody(
    tasks: List<TaskUIModel>,
    taskListViewModel: TaskListViewModel,
    navigationController: NavHostController
) {

    Scaffold(
        topBar = { MyTopAppBar(onLogOut = taskListViewModel::logOut) },
        floatingActionButton = { MyFAB { navigationController.navigate(Routes.AddTask(0L)) } },
        floatingActionButtonPosition = FabPosition.End
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .padding(contentPadding)

        ) {
            WelcomeUserHeader()
            CardTaskBody(tasks, taskListViewModel, navigationController)
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(onLogOut: () -> Unit) {
    TopAppBar(
        title = { Text(text = "TaskApp") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Blue,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        actions = {
            IconButton(onClick = onLogOut) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "options")
            }
        })

}

@Composable
fun MyFAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
    }
}

@Composable
private fun CardTaskBody(
    tasks: List<TaskUIModel>,
    taskListViewModel: TaskListViewModel,
    navigationController: NavHostController
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
            TasksList(tasks, taskListViewModel, navigationController)
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


@Composable
private fun TasksList(
    tasks: List<TaskUIModel>,
    taskListViewModel: TaskListViewModel,
    navigationController: NavHostController
) {

    LazyColumn {

        items(tasks, key = { it.id }) {

            ItemTask(
                Modifier
                    .animateItem(
                        fadeInSpec = null, fadeOutSpec = null, placementSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .clickable { navigationController.navigate(Routes.AddTask(id = it.id)) },
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
        onDelete = { taskListViewModel.onItemRemove(taskModel) },
        paddingValues = PaddingValues(vertical = 8.dp),
        cornerShape = RoundedCornerShape(12.dp)
    ) {
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
    paddingValues: PaddingValues = PaddingValues(0.dp),
    cornerShape: RoundedCornerShape = RoundedCornerShape(0.dp),
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
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
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(
                    swipeDismissState = state,
                    paddingValues = paddingValues,
                    cornerShape = cornerShape
                )
            },
            content = { content(item) },
            enableDismissFromEndToStart = true
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState,
    paddingValues: PaddingValues,
    cornerShape: RoundedCornerShape
) {
    val color = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .clip(cornerShape)
            .background(color),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            modifier = Modifier.padding(6.dp),
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}

