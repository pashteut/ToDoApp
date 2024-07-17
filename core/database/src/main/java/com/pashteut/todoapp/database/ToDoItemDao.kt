package com.pashteut.todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pashteut.todoapp.database.dbos.TodoItemDBO
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for ToDoItem entities.
 *
 * This interface defines the database operations that can be performed on ToDoItem entities. It includes
 * methods for observing all ToDo items, fetching them by ID, inserting, updating, and deleting ToDo items.
 * The operations support both synchronous and asynchronous execution through Kotlin Coroutines and Flow.
 *
 * Functions:
 * - observeAll: Returns a Flow of a list of all ToDo items for real-time updates.
 * - getAll: retrieves all ToDo items.
 * - getById: fetches a single ToDo item by its ID.
 * - getFlowById: Returns a Flow of a ToDo item for real-time updates by its ID.
 * - delete: Deletes a specific ToDo item.
 * - delete(id: String): Deletes a ToDo item by its ID.
 * - deleteAll: Deletes all ToDo items from the database.
 * - insert: Inserts a new ToDo item, replacing it if it already exists.
 * - update: Updates an existing ToDo item.
 * - insertAll: Inserts a list of ToDo items, replacing them if they already exist.
 */

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM to_do_items")
    fun observeAll() : Flow<List<TodoItemDBO>>

    @Query("SELECT * FROM to_do_items")
    suspend fun getAll() : List<TodoItemDBO>

    @Query("SELECT * FROM to_do_items WHERE id = :id")
    suspend fun getById(id: String) : TodoItemDBO?

    @Query("SELECT * FROM to_do_items WHERE id = :id")
    fun getFlowById(id: String) : Flow<TodoItemDBO?>

    @Delete
    suspend fun delete(item: TodoItemDBO)

    @Query("DELETE FROM to_do_items WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM to_do_items")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TodoItemDBO) : Long

    @Update
    suspend fun update(item: TodoItemDBO)

    @Insert
    suspend fun insertAll(items: List<TodoItemDBO>)
}