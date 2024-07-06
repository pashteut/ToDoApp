package com.pashteut.todoapp.presentator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.common.UIMessagesStorage
import com.pashteut.todoapp.model.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the Main Screen of the ToDo application.
 *
 * Manages UI-related data for the Main Screen, handling operations such as fetching ToDo items from the repository,
 * updating visibility of done items, and managing UI error states. It interacts with [TodoItemsRepository] for data
 * operations and [UIMessagesStorage] for error handling.
 *
 * @property repository Instance of [TodoItemsRepository] for data operations.
 * @property appDispatchers Provides Coroutine Dispatchers for background operations.
 * @property uiMessage Instance of [UIMessagesStorage] for managing UI messages.
 */

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val appDispatchers: AppDispatchers,
    private val uiMessage: UIMessagesStorage,
) : ViewModel() {

    private val _doneItemsVisibility = MutableStateFlow(false)
    val doneItemsVisibility = _doneItemsVisibility.asStateFlow()

    private val _doneCount = MutableStateFlow(0)
    val doneCount = _doneCount.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val userMessage = uiMessage.message

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("MainScreenViewModel", "Coroutine exception", exception)
    }
    private val scope = viewModelScope + handler
    private val repositoryScope = CoroutineScope(SupervisorJob() + appDispatchers.io + handler)

    fun changeDoneItemsVisibility() {
        _doneItemsVisibility.value = !_doneItemsVisibility.value
    }

    init {
        scope.launch {
            withContext(appDispatchers.io) {
                try {
                    repository.syncChanges()
                } catch (e: Exception) {
                    uiMessage.setMessage(UIMessagesStorage.UIMessage.LoadError)
                    throw e
                }
            }
        }
    }

    val toDoItems = repository.getAllItems()
        .combine(doneItemsVisibility) { list, visibility ->
            _doneCount.value = list.filter { item -> item.isDone }.size

            if (visibility) list.sortedByDescending { it.updatedDate ?: it.createdDate }
            else list
                .filter { item -> !item.isDone }
                .sortedByDescending { it.updatedDate ?: it.createdDate }
        }
        .stateIn(scope, SharingStarted.Lazily, emptyList())

    fun deleteItem(id: String) =
        repositoryScope.launch(appDispatchers.io) {
            try {
                repository.deleteItem(id)
            } catch (e: Exception) {
                uiMessage.setMessage(UIMessagesStorage.UIMessage.DeleteError)
                throw e
            }
        }

    fun changeIsDone(id: String) {
        repositoryScope.launch(appDispatchers.io) {
            Log.d("MainScreenViewModel", "Changing isDone for item $id")
            try {
                repository.changeIsDone(id)
            } catch (e: Exception) {
                uiMessage.setMessage(UIMessagesStorage.UIMessage.UpdateError)
                throw e
            }
        }
    }

    fun refreshItems() {
        repositoryScope.launch {
            _isRefreshing.value = true
            withContext(appDispatchers.io) {
                try {
                    repository.syncChanges()
                } catch (e: Exception) {
                    Log.e("MainScreenViewModel", "Error fetching items", e)
                    uiMessage.setMessage(UIMessagesStorage.UIMessage.LoadError)
                }
            }
            _isRefreshing.value = false
        }
    }
}