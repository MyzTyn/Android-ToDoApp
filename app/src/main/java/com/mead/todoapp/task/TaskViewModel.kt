package com.mead.todoapp.task

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mead.todoapp.data.ITaskRepository
import com.mead.todoapp.data.TaskDatabase
import com.mead.todoapp.data.TaskEntityModel
import com.mead.todoapp.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository : ITaskRepository
    val tasks: StateFlow<List<TaskEntityModel>>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDao)
        tasks = taskRepository.getStream().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

        // Check if the database is empty
        viewModelScope.launch(Dispatchers.IO) {
            if (taskRepository.count() != 0)
                return@launch

            // Create some tasks
            create(TaskEntityModel(name = "Code Review", description = "Check the C# project and give comments"))
            create(TaskEntityModel(name = "ToDo App", description = "Make the app looks pretty and nice"))
            create(TaskEntityModel(name = "Hello"))
        }
    }

    fun create(task: TaskEntityModel) {
        Log.i("TaskViewModel", "create: ${task.id}")
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.create(task.name, task.description)
        }
    }

    fun insert(task: TaskEntityModel) {
        Log.i("TaskViewModel", "insert: ${task.id}")
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insert(task)
        }
    }

    fun updateCompletion(id: UUID, isCompleted: Boolean) {
        Log.i("TaskViewModel", "updateComplete: ${id}")
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateCompletionByID(id, isCompleted)
        }
    }

    fun delete(task: TaskEntityModel) {
        Log.i("TaskViewModel", "delete: ${task.id}")
        viewModelScope.launch(Dispatchers.IO) {
            // ToDo: Remove the delay then add an animation
            delay(500)
            taskRepository.delete(task)
        }
    }
}