package com.mead.todoapp.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

class TaskRepository(
    private val localDataSource: TaskDao
) : ITaskRepository {

    override fun getStream(): Flow<List<TaskEntityModel>> {
        return localDataSource.observeAll()
    }

    override suspend fun getByID(id: UUID): TaskEntityModel? {
        return localDataSource.getByID(id)
    }

    override suspend fun create(title: String, description: String): UUID {
        return create(TaskEntityModel(
            name = title,
            description = description
        ))
    }

    override suspend fun create(task: TaskEntityModel): UUID {
        localDataSource.insertTask(task)
        return task.id
    }

    override suspend fun insert(task: TaskEntityModel) {
        if (getByID(task.id) == null)
            throw TaskNotFoundException(task.id)

        localDataSource.insertTask(task)
    }

    override suspend fun updateCompletionByID(id: UUID, isCompleted: Boolean) {
        if (getByID(id) == null)
            throw TaskNotFoundException(id)

        localDataSource.updateCompletionByID(id, isCompleted)
    }

    override suspend fun delete(task: TaskEntityModel) {
        localDataSource.delete(task)
    }

    override suspend fun count(): Int {
        return localDataSource.count()
    }
}

// Custom Task Exception
class TaskNotFoundException(id: UUID) : Exception("Task (id ${id}) not found.")