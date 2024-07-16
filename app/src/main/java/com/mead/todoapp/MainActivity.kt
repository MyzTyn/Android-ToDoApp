package com.mead.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.mead.todoapp.task.TaskListScreen
import com.mead.todoapp.task.TaskViewModel
import com.mead.todoapp.ui.theme.ToDoAppTheme

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoAppTheme {
                TaskListScreen(taskViewModel)
            }
        }
    }
}