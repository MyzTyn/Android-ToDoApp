package com.mead.todoapp.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.mead.todoapp.data.TaskEntityModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(taskViewModel: TaskViewModel = viewModel()) {
    // Observe the tasks LiveData
    val tasks: List<TaskEntityModel> by taskViewModel.tasks.observeAsState(emptyList())

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
        },
        floatingActionButton = {
            // ToDo: Implement the add task
            FloatingActionButton(onClick = {
                println("The Add Button is pressed! Task Creation is not implemented")
            }) {
                Icon(
                    Icons.Default.Add,
                    "Add Task")
            }
        }
    ) {
        padding -> run {
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(15.dp)
        ) {
            items(tasks) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange =  { isChecked ->
                        run {
                            println("The Task (${task.id}) onCheckedChange: $isChecked")
                            val updatedTask = task.copy(isCompleted = isChecked)
                            taskViewModel.insert(updatedTask)

                            // ToDo: hide the task instead of removal
                            // taskViewModel.delete(updatedTask)
                        }
                    }
                )
            }
        }

    }
}
}

class TaskProvider : PreviewParameterProvider<TaskEntityModel> {
    override val values = listOf(
        TaskEntityModel(
            name = "Code C#",
            description = "Pokemon Stadium"
        ),
        TaskEntityModel(
            name = "Make a food",
            description = "Find a recipe for the food"
        )
    ).asSequence()
}

@Preview(showBackground = true)
@Composable
fun TaskItem(@PreviewParameter(TaskProvider::class) task: TaskEntityModel, onCheckedChange: ((Boolean) -> Unit)? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
    ) {
        Checkbox(checked = task.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Column {
            Text(
                text = task.name,
                style = MaterialTheme.typography.labelLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            if (task.description.isNotEmpty())
            {
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}