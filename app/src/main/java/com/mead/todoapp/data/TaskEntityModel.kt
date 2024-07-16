package com.mead.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class TaskEntityModel(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var name: String = "",
    var description: String = "",
    var isCompleted: Boolean = false,
)