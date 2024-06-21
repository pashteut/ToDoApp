package com.pashteut.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM to_do_items")
    fun observeAll() : Flow<List<ToDoItem>>

    @Query("SELECT * FROM to_do_items")
    suspend fun getAll() : List<ToDoItem>

    @Query("SELECT * FROM to_do_items WHERE id = :id")
    suspend fun getById(id: Long) : ToDoItem

    @Delete
    suspend fun delete(item: ToDoItem)

    @Query("DELETE FROM to_do_items WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM to_do_items")
    suspend fun deleteAll()

    @Insert
    suspend fun insert(item: ToDoItem) : Long

    @Update
    suspend fun update(item: ToDoItem)

    @Insert
    suspend fun insertAll(items: List<ToDoItem>)
}