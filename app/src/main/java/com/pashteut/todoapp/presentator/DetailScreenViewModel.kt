package com.pashteut.todoapp.presentator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.model.Priority
import com.pashteut.todoapp.model.ToDoItem
import com.pashteut.todoapp.model.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    val text = MutableStateFlow("")
    val priority = MutableStateFlow(Priority.DEFAULT)
    var deadLineTime = MutableStateFlow<Long?>(null)
    private var pickedItem: ToDoItem? = null

    fun saveToDoItem(): Boolean {
        if (text.value.trim().isNotEmpty()) {
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
        } else
            return false
        return true
    }

    fun deleteItem() {
        pickedItem?.let {
            repository.deleteItem(it.id)
        }
    }

    fun setPickedItem(id: Long) {
        if (id != -1L) {
            viewModelScope.launch {
                repository.getItem(id).also {
                    pickedItem = it
                    text.value = it.text
                    priority.value = it.priority
                    deadLineTime.value = it.deadline
                }
            }
        }
    }

}