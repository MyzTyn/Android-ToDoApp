package com.mead.todoapp.data

import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ITaskRepository {
    /**
     * Get the stream of tasks
     */
    fun getStream(): Flow<List<TaskEntityModel>>

    /**
     * Get the task by ID (UUID)
     */
    suspend fun getByID(id: UUID) : TaskEntityModel?

    /**
     * Create a task
     * @param title
     * @param description
     * @return the ID of the task
     */
    suspend fun create(title: String, description: String): UUID
    /**
     * Create a task
     * @param task TaskEntityModel
     * @return the ID of the task
     */
    suspend fun create(task: TaskEntityModel): UUID

    /**
     * Update the task
     * @param task TaskEntityModel
     */
    suspend fun insert(task: TaskEntityModel)

    /**
     * Delete the task
     * @param task TaskEntityModel
     */
    suspend fun delete(task: TaskEntityModel)

    /**
     * The size of task
     */
    suspend fun count() : Int
}