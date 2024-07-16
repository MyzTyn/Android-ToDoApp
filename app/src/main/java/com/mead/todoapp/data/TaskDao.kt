package com.mead.todoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface TaskDao {
    /**
     * Observes list of tasks
     */
    @Query("SELECT * FROM tasks")
    fun observeAll(): Flow<List<TaskEntityModel>>

    /**
     * Get the task by ID (UUID)
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getByID(id: UUID) : TaskEntityModel?

    /**
     * Insert or update a task in the database
     */
    @Upsert
    suspend fun insertTask(task: TaskEntityModel)

    /**
     * Delete the task
     */
    @Delete
    suspend fun deleteTask(task: TaskEntityModel)

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun count(): Int
}