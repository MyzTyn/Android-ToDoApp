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

    @Query("UPDATE tasks SET isCompleted = :completion WHERE id = :id")
    suspend fun updateCompletionByID(id: UUID, completion: Boolean)

    /**
     * Delete the task
     */
    @Delete
    suspend fun delete(task: TaskEntityModel)

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun count(): Int
}