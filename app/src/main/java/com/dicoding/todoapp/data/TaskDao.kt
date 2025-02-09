package com.dicoding.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.dicoding.todoapp.utils.FilterUtils

//TODO 2 : Define data access object (DAO)

@Dao
interface TaskDao {
    @RawQuery
    fun getTasks(query: SupportSQLiteQuery): PagingSource<Int, Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId ")
    fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM tasks WHERE dueDate >= date('now') ORDER BY dueDate ASC")
    fun getNearestActiveTask(): Task

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg tasks: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateCompleted(taskId: Int, completed: Boolean)
    
}
