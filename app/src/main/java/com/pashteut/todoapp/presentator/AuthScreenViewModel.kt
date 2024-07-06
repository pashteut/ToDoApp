package com.pashteut.todoapp.presentator

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pashteut.todoapp.common.AppDispatchers
import com.pashteut.todoapp.common.UIMessagesStorage
import com.pashteut.todoapp.model.TodoItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

/**
 * ViewModel for the Authentication Screen of the ToDo application.
 *
 * Manages the authentication state, handling operations such as setting OAuth tokens for user authentication,
 * and managing the success or failure state of these operations. It interacts with [TodoItemsRepository] for
 * authentication operations and updates UI state based on the outcome of these operations.
 *
 * @property repository Instance of [TodoItemsRepository] for authentication operations.
 * @property appDispatchers Provides Coroutine Dispatchers for background operations.
 * @property uiMessage Instance of [UIMessagesStorage] for managing UI messages.
 */

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val repository: TodoItemsRepository,
    private val appDispatchers: AppDispatchers,
    private val uiMessage: UIMessagesStorage,
) : ViewModel() {

    val message = uiMessage.message

    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.e("DetailScreenViewModel", "Coroutine exception", exception)
    }
    private val scope = viewModelScope + handler

    fun setToken(token: String) {
        scope.launch(appDispatchers.io) {
            val authResult = repository.setOAuthToken(token)
            if (authResult)
                uiMessage.setMessage(UIMessagesStorage.UIMessage.AuthorizationSuccess)
            else
                uiMessage.setMessage(UIMessagesStorage.UIMessage.AuthorizationError)
        }
    }

    fun logout() =
        scope.launch(appDispatchers.io) {
            repository.logout()
            uiMessage.setMessage(UIMessagesStorage.UIMessage.AuthorizationReset)
        }
}
