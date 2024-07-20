package com.mead.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mead.todoapp.data.TaskEntityModel
import com.mead.todoapp.task.AddTaskScreen
import com.mead.todoapp.task.TaskListScreen
import com.mead.todoapp.task.TaskViewModel
import com.mead.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                TaskApp()
            }
        }
    }
}

/**
 * Represents the different screens or states within the Task App navigation flow.
 */
enum class ScreenState {
    TASK_LIST,
    ADD_TASK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskApp(taskViewModel: TaskViewModel = viewModel()) {
    val navController = rememberNavController()

    // The main layout of the app
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
