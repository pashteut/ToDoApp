package com.pashteut.todoapp.presentator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.common.UIMessagesStorage
import com.pashteut.todoapp.common.convertDateToString
import com.pashteut.todoapp.model.Priority
import com.pashteut.todoapp.model.ToDoItem
import com.pashteut.todoapp.model.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for the Detail Screen of the ToDo application.
 *
 * Manages the details of a single ToDo item, including its text, priority, and deadline. Supports operations
 * such as updating an existing item or creating a new one, setting the deadline, and deleting the item.
 * It interacts with [TodoItemsRepository] for data operations and [UIMessagesStorage] for error handling.
 *
 * @property repository Instance of [TodoItemsRepository] for data operations.
 * @property appDispatchers Provides Coroutine Dispatchers for background operations.
 * @property uiMessage Instance of [UIMessagesStorage] for managing UI messages.
 */

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val appDispatchers: AppDispatchers,
    private val uiMessage: UIMessagesStorage,
) : ViewModel() {

    private val _text = MutableStateFlow("")
    val text get() = _text.asStateFlow()

    private val _priority = MutableStateFlow(Priority.DEFAULT)
    val priority get() = _priority.asStateFlow()

    private val deadLineTime = MutableStateFlow<Long?>(null)

    private val _textDate = MutableStateFlow("")
    val textDate = _textDate.asStateFlow()

    private var pickedItem: ToDoItem? = null
        set(value) {
            field = value
            value?.let {
                _text.update { value.text }
                _priority.update { value.priority }
                deadLineTime.update { value.deadline }
            }
        }

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("DetailScreenViewModel", "Coroutine exception", exception)
    }
    private val scope = viewModelScope + handler
    private val repositoryScope = CoroutineScope(SupervisorJob() + appDispatchers.io + handler)

    init {
        scope.launch {
            deadLineTime.collect { deadLine ->
                _textDate.value =
                    withContext(appDispatchers.default) { deadLine?.convertDateToString() }
                        ?: ""
            }
        }
    }

    fun setDeadLineTime(value: Long?) {
        deadLineTime.update { value }
    }

    fun setPriority(value: Priority) {
        _priority.update { value }
    }

    fun setText(value: String) {
        _text.update { value }
    }

    fun saveToDoItem(): Boolean {
        if (text.value.trim().isEmpty())
            return false

        repositoryScope.launch {
            pickedItem?.let {
                try {
                    repository.updateItem(
                        it.copy(
                            text = text.value.trim(),
                            priority = priority.value,
                            deadline = deadLineTime.value,
                            updatedDate = System.currentTimeMillis()
                        )
                    )
                } catch (e: Exception) {
                    uiMessage.setMessage(UIMessagesStorage.UIMessage.UpdateError)
                    throw e
                }
            } ?: try {
                repository.saveItem(
                    ToDoItem(
                        text = text.value.trim(),
                        priority = priority.value,
                        deadline = deadLineTime.value,
                        isDone = false,
                        createdDate = System.currentTimeMillis()
                    )
                )
            } catch (e: Exception) {
                uiMessage.setMessage(UIMessagesStorage.UIMessage.SaveError)
                throw e

            }
        }
        return true
    }

    fun deleteItem() {
        pickedItem?.let {
            repositoryScope.launch {
                try {
                    repository.deleteItem(it.id)
                } catch (e: Exception) {
                    uiMessage.setMessage(UIMessagesStorage.UIMessage.DeleteError)
                    throw e
                }
            }
        }
    }

    fun setPickedItem(id: String?) {
        id?.let {
            scope.launch {
                val pickedItemFlow = repository.getItem(id)
                pickedItemFlow.collect { item ->
                    pickedItem = item
                }
            }
        }
    }
}