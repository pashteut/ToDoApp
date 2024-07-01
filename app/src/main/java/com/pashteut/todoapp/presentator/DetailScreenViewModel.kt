package com.pashteut.todoapp.presentator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.common.convertDateToString
import com.pashteut.todoapp.model.Priority
import com.pashteut.todoapp.model.ToDoItem
import com.pashteut.todoapp.model.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val appDispatchers: AppDispatchers,
) : ViewModel() {

    val text = MutableStateFlow("")
    val priority = MutableStateFlow(Priority.DEFAULT)
    val deadLineTime = MutableStateFlow<Long?>(null)
    private val _textDate = MutableStateFlow("")
    val textDate = _textDate.asStateFlow()
    private var pickedItem: ToDoItem? = null

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("DetailScreenViewModel", "Coroutine exception", exception)
    }
    private val scope = viewModelScope + handler

    init {
        scope.launch {
            deadLineTime.collect { deadLine ->
                _textDate.value =
                    withContext(appDispatchers.default) { deadLine?.convertDateToString() }
                        ?: ""
            }
        }
    }

    fun saveToDoItem(): Boolean {
        if (text.value.trim().isEmpty())
            return false

        scope.launch(appDispatchers.io) {
            pickedItem?.let {
                repository.updateItem(
                    it.copy(
                        text = text.value.trim(),
                        priority = priority.value,
                        deadline = deadLineTime.value,
                        updatedDate = System.currentTimeMillis()
                    )
                )
            } ?: repository.saveItem(
                ToDoItem(
                    text = text.value.trim(),
                    priority = priority.value,
                    deadline = deadLineTime.value,
                    isDone = false,
                    createdDate = System.currentTimeMillis()
                )
            )
        }
        return true
    }

    fun deleteItem() {
        pickedItem?.let {
            scope.launch(appDispatchers.io) {
                repository.deleteItem(it.id)
            }
        }
    }

    fun setPickedItem(id: Long) {
        if (id != -1L) {
            scope.launch(appDispatchers.io) {
                async { repository.getItem(id) }
                    .await()
                    .also {
                        pickedItem = it
                        text.value = it.text
                        priority.value = it.priority
                        deadLineTime.value = it.deadline
                    }
            }
        }
    }
}