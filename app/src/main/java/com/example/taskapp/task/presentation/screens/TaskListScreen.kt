package com.example.taskapp.task.presentation.screens

import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.taskapp.R
import com.example.taskapp.core.presentation.components.CircleBackground
import com.example.taskapp.core.presentation.components.ErrorComponent
import com.example.taskapp.core.presentation.components.LoadingComponent
import com.example.taskapp.core.presentation.components.SwipeToDeleteContainer
import com.example.taskapp.core.presentation.utils.asUiText
import com.example.taskapp.core.routes.Routes
import com.example.taskapp.task.domain.enum.TaskStatus
import com.example.taskapp.task.presentation.components.SingleRowCalendarWithHorizontalScroll
import com.example.taskapp.task.presentation.model.MenuOption
import com.example.taskapp.task.presentation.model.TaskUIModel
import com.example.taskapp.task.presentation.state.TaskListUIState
import com.example.taskapp.task.presentation.viewmodel.TaskListViewModel
import com.example.taskapp.ui.theme.BlueLight
import com.example.taskapp.ui.theme.Green
import com.example.taskapp.ui.theme.Lavender
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@Composable
fun TaskScreen(taskListViewModel: TaskListViewModel, navigationController: NavHostController) {

    val context = LocalContext.current

    LaunchedEffect(true) {
        taskListViewModel.getTasks()
    }


    LaunchedEffect(key1 = taskListViewModel.taskDeletedEvent) {
        taskListViewModel.taskDeletedEvent.collectLatest {
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

    LaunchedEffect(key1 = taskListViewModel.taskStatusEvent) {
        taskListViewModel.taskStatusEvent.collectLatest {
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
            ErrorComponent(
                text = "An error has occurred",
                reload = taskListViewModel::getTasks,
                hasBackButton = false,
                onBackPressed = {})
        }

        TaskListUIState.Loading -> {
            LoadingComponent()
        }

        is TaskListUIState.Success -> {
            MainBody(
                (uiState as TaskListUIState.Success),
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
    uiState: TaskListUIState.Success,
    taskListViewModel: TaskListViewModel,
    navigationController: NavHostController
) {


    CircleBackground(color = MaterialTheme.colorScheme.primary) {
        Scaffold(
            topBar = { MyTopAppBar(uiState.userName, taskListViewModel) },
            floatingActionButton = { MyFAB { navigationController.navigate(Routes.AddTaskScreen(0L)) } },
            floatingActionButtonPosition = FabPosition.End,
            containerColor = Color.Transparent
        ) { contentPadding ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //calendar

                Spacer(modifier = Modifier.height(32.dp))
                SingleRowCalendarWithHorizontalScroll(
                    selectedDate = uiState.selectedDate,
                    yearMonth = uiState.yearMonth,
                    onDateChange = taskListViewModel::changeSelectedDate,
                    generateDaysInMonth = taskListViewModel::generateDaysInMonth,
                    generateDaysInMonthWithTaskCount = taskListViewModel::generateDaysInMonthWithTaskCount,
                    calculateScrollOffset = taskListViewModel::calculateScrollOffset,
                    previousMonth = { taskListViewModel.changeMonth(false) },
                    nextMonth = { taskListViewModel.changeMonth(true) }
                )

                HorizontalDivider(Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                //task list
                if (uiState.tasks.isEmpty()) {
                    NoTaskText()
                } else {
                    TaskLazyList(uiState, navigationController, taskListViewModel)

                }
            }

        }
    }

}

@Composable
private fun TaskLazyList(
    uiState: TaskListUIState.Success,
    navigationController: NavHostController,
    taskListViewModel: TaskListViewModel
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(uiState.tasks, key = { it.id }) {
            ItemTask(
                Modifier
                    .animateItem(
                        fadeInSpec = null,
                        fadeOutSpec = null,
                        placementSpec = spring(
                            dampingRatio = Spring.DampingRatioHighBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    .clickable { navigationController.navigate(Routes.DetailScreen(id = it.id)) },
                taskModel = it, taskListViewModel = taskListViewModel
            )
        }
    }
}

@Composable
private fun NoTaskText() {

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_tasks_this_day),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(userName: String, taskListViewModel: TaskListViewModel) {

    var expanded by remember { mutableStateOf(false) }

    val menuList = listOf(
        MenuOption(
            stringResource(R.string.log_out),
            Icons.AutoMirrored.Rounded.Logout
        ) { taskListViewModel.logOut() })

    TopAppBar(
        title = { WelcomeUserHeader(userName = userName) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "options")
            }
            PopUpMenuList(expanded = expanded, items = menuList, onDismiss = { expanded = false })
        })


}

@Composable
fun PopUpMenuList(expanded: Boolean, items: List<MenuOption>, onDismiss: () -> Unit) {

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.clip(RoundedCornerShape(50.dp))
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                onClick = {
                    item.onClick()
                    onDismiss()
                },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = item.icon, contentDescription = item.name)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(item.name)
                    }
                }
            )
        }
    }
}

@Composable
fun MyFAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "add")
    }
}


@Composable
private fun WelcomeUserHeader(userName: String) {
    Row(
        Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.hello) + ",",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = userName,
            style = MaterialTheme.typography.titleSmall
        )
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
        TaskCard(
            modifier = modifier,
            taskModel = taskModel,
            onCheckedChange = taskListViewModel::updateTaskStatus
        )
    }
}

@Composable
private fun TaskCard(
    modifier: Modifier,
    taskModel: TaskUIModel,
    onCheckedChange: (Boolean, Long) -> Unit
) {

    val color = when (taskModel.status) {
        TaskStatus.PENDING -> MaterialTheme.colorScheme.tertiary
        TaskStatus.IN_PROGRESS -> BlueLight
        TaskStatus.COMPLETED -> Green
    }
    Card(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = taskModel.title, modifier = Modifier
                        .padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                Row {
                    Text(
                        text = taskModel.dueDate.toLocalTime()
                            .format(DateTimeFormatter.ofPattern("HH:mm")), modifier = Modifier
                            .padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(text = "·", style = MaterialTheme.typography.bodySmall)
                    Text(
                        text = taskModel.status.asUiText().asString(),
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

            }

            Checkbox(
                checked = taskModel.status == TaskStatus.COMPLETED,
                onCheckedChange = { onCheckedChange(it, taskModel.id) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = Lavender
                )
            )

        }
    }
}





