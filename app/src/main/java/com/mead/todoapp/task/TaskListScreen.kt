package com.mead.todoapp.task

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.mead.todoapp.data.TaskEntityModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class ScreenState {
    TASK_LIST,
    ADD_TASK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(taskViewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = Color.Transparent,
                    actionIconContentColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                title = {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = "To Do List"
                    )
                }
            )
        }
    ) { innerPadding ->
        run {
            NavHost(navController = navController, startDestination = ScreenState.TASK_LIST.name) {
                composable(ScreenState.TASK_LIST.name) {
                    TaskListScreen(
                        innerPadding = innerPadding,
                        taskViewModel = taskViewModel,
                        createTaskAction = {
                            navController.navigate(ScreenState.ADD_TASK.name)
                        })
                }
                composable(ScreenState.ADD_TASK.name) {
                    AddTaskScreen(innerPadding = innerPadding,
                        cancelButton = {
                        navController.navigateUp()
                    }, addTaskAction = { title, description ->
                        taskViewModel.create(
                            TaskEntityModel(
                                name = title,
                                description = description
                            )
                        )
                        navController.navigateUp()
                    })
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(innerPadding: PaddingValues = PaddingValues(0.dp), taskViewModel: TaskViewModel = viewModel(), createTaskAction: (() -> Unit)? = null) {
    // Observe the tasks StateFlow
    val tasks by taskViewModel.tasks.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.padding(innerPadding),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                createTaskAction?.invoke()
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) {
        LazyColumn(
        modifier = Modifier
            .padding(15.dp)
        ) {
            items(tasks, key = { item -> item.id }) { task ->
                ListTaskItem(
                    task = task,
                    onCheckedChange = { isChecked ->
                        run {
                            Log.i("ListTaskItem", "The Task (${task.id}) onCheckedChange: $isChecked")
                            taskViewModel.updateCompletion(task.id, isChecked)
                        }
                    },
                    confirmValueChange = {
                        when (it) {
                            SwipeToDismissBoxValue.EndToStart -> {
                                taskViewModel.delete(task)
                            }

                            else -> return@ListTaskItem false
                        }
                        return@ListTaskItem true
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }


}

/**
 * Add Task Screen where user can input the name and description of the task
 */
@Preview(showBackground = true)
@Composable
fun AddTaskScreen(innerPadding: PaddingValues = PaddingValues(0.dp), cancelButton: (() -> Unit)? = null,
                  addTaskAction: ((title: String, description: String) -> Unit)? = null) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
    ) {
        // Cancel Button
        Button(onClick = { cancelButton?.invoke() },
            modifier = Modifier.align(Alignment.Start),
            shape = ButtonDefaults.outlinedShape) {
            Text("Cancel")
        }

        OutlinedTextField(value = title,
            onValueChange = { title = it },
            label = { Text("Title")},
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)")},
            modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        // Add to the database
        Button(onClick = { addTaskAction?.invoke(title, description) },
            modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Add Task")
        }
    }
}

class TaskProvider : PreviewParameterProvider<TaskEntityModel> {
    override val values = listOf(
        TaskEntityModel(
            name = "Code C#",
            description = "Pokemon Stadium, there some features that I need to implement"
        ),
        TaskEntityModel(
            name = "Make a food",
            description = "Find a recipe for the food"
        )
    ).asSequence()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ListTaskItem(@PreviewParameter(TaskProvider::class) task: TaskEntityModel, onCheckedChange: ((Boolean) -> Unit)? = null, confirmValueChange: ((SwipeToDismissBoxValue) -> Boolean)? = null)
{
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange  = { confirmValueChange?.invoke(it) ?: true },
        positionalThreshold = { it * 0.35f }
    )

    SwipeToDismissBox(state = dismissState,
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFFF1744)
                    else -> Color.Transparent
                }
            )
            val icon = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Delete
                else -> null
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 16.dp)
                    )
                }
            }
        },
        enableDismissFromStartToEnd = false,
    ) {
        ElevatedCard {
            ListItem(
                headlineContent = {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.labelLarge,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                supportingContent = {
                    if (task.description.isNotEmpty())
                    {
                        Text(
                            text = task.description,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }
                },
                trailingContent = {
                    Checkbox(checked = task.isCompleted,
                        onCheckedChange = onCheckedChange
                    )
                },
            )
        }

    }
}