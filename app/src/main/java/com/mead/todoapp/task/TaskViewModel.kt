package com.mead.todoapp.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mead.todoapp.data.ITaskRepository
import com.mead.todoapp.data.TaskDatabase
import com.mead.todoapp.data.TaskEntityModel
import com.mead.todoapp.data.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskRepository : ITaskRepository
    val tasks: LiveData<List<TaskEntityModel>>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        taskRepository = TaskRepository(taskDao)
        tasks = taskRepository.getStream().asLiveData()

        viewModelScope.launch(Dispatchers.IO) {
            if (taskRepository.count() == 0) {
                create(TaskEntityModel(name = "Code Review", description = "Check the C# project and give comments"))
                create(TaskEntityModel(name = "ToDo App", description = "Make the app looks pretty and nice"))
                create(TaskEntityModel(name = "Hello"))
            }
        }
    }

    fun create(task: TaskEntityModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.create(task.name, task.description)
        }
    }

    fun insert(task: TaskEntityModel) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insert(task)
        }
    }

    fun delete(task: TaskEntityModel) {
        viewModelScope.launch(Dispatchers.IO) {
            // Some delays
            delay(1000)
            taskRepository.delete(task)
        }
    }
}