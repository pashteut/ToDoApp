package com.pashteut.todoapp.presentator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.model.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val appDispatchers: AppDispatchers
) : ViewModel() {

    private val _doneItemsVisibility = MutableStateFlow(false)
    val doneItemsVisibility = _doneItemsVisibility.asStateFlow()

    private val _doneCount = MutableStateFlow(0)
    val doneCount = _doneCount.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("MainScreenViewModel", "Coroutine exception", exception)
    }
    private val scope = viewModelScope + handler

    fun changeDoneItemsVisibility() {
        _doneItemsVisibility.value = !_doneItemsVisibility.value
    }

    val toDoItems = repository.getAllItems()
        .combine(doneItemsVisibility) { list, visibility ->
            _doneCount.value = list.filter { item -> item.isDone }.size

            if (visibility) list
            else list.filter { item -> !item.isDone }
        }
        .stateIn(scope, SharingStarted.Lazily, emptyList())

    fun deleteItem(id: Long) =
        scope.launch(appDispatchers.io) {
            repository.deleteItem(id)
        }

    fun changeIsDone(id: Long) =
        scope.launch(appDispatchers.io) {
            repository.changeIsDone(id)
        }
}