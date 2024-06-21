package com.pashteut.todoapp.presentator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.model.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository
) : ViewModel() {

    var doneItemsVisibility = MutableStateFlow(false)

    val toDoItems = repository.getAllItems()
        .combine(doneItemsVisibility){ list, visibility ->
            doneCount.value = list.filter { item -> item.isDone }.size

            if (visibility) list
            else list.filter { item -> !item.isDone }
        }

    var doneCount = MutableStateFlow(0)

    fun deleteItem(id: Long) =
        viewModelScope.launch {
            repository.deleteItem(id)
        }

    fun changeIsDone(id: Long) =
        viewModelScope.launch {
            repository.changeIsDone(id)
        }
}