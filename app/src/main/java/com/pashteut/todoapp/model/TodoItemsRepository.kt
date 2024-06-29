package com.pashteut.todoapp.model

import com.pashteut.todoapp.common.AppDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    private val database: ToDoItemDao,
    private val appDispatchers: AppDispatchers,
) {


    fun getAllItems() = database.observeAll()

    suspend fun getItem(id: Long) =
        withContext(appDispatchers.io) {
            database.getById(id)
        }

    suspend fun saveItem(item: ToDoItem) =
        withContext(appDispatchers.io) {
            database.insert(item)
        }

    suspend fun updateItem(item: ToDoItem) =
        withContext(appDispatchers.io) {
            database.update(item)
        }


    suspend fun deleteItem(id: Long) =
        withContext(appDispatchers.io) {
            database.delete(id)
        }

    suspend fun changeIsDone(id: Long) =
        withContext(appDispatchers.io) {
            val item = database.getById(id)
            database.update(item.copy(isDone = !item.isDone))
        }
}