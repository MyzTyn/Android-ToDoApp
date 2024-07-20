package com.mead.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class TaskEntityModel(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
)