package com.pashteut.todoapp.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoItemsRepository @Inject constructor(
    private val database: ToDoItemDao
) {

    fun getAllItems() = database.observeAll()

    suspend fun getItem(id: Long)  =
        CoroutineScope(Dispatchers.IO)
            .async { database.getById(id) }
            .await()

    fun saveItem(item: ToDoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            database.insert(item)
        }
    }
    fun updateItem(item: ToDoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            database.update(item)
        }
    }

    fun deleteItem(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            database.delete(id)
        }
    }
    fun deleteItem(item: ToDoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            database.delete(item)
        }
    }

    fun markIsDone(position: Int, isDone: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            database.update(getAllItems().first()[position].copy(isDone = isDone))
        }
    }

    fun changeIsDone(id : Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val item = database.getById(id)
            database.update(item.copy(isDone = !item.isDone))
        }
    }
}