package com.dicoding.todoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO 1 : Define a local database table using the schema in app/schema/tasks.json
@Entity("tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo("title")
    val  title: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("dueDate")
    val dueDateMillis: Long,
    @ColumnInfo("completed")
    val isCompleted: Boolean = false
)
