package com.pashteut.todoapp.features.common

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Manages the storage and update of UI messages, including errors and notifications.
 *
 * This class is designed to handle UI messages that need to be displayed to the user, such as errors and success notifications.
 * It uses a [MutableStateFlow] to maintain the current message and an error counter to track the number of errors encountered.
 * The messages are updated and can be observed by the UI components to inform the user of the current state or any issues.
 *
 * @property message A state flow that emits the current UI message (error or notification) along with the total count of errors encountered.
 */

class UIMessagesStorage @Inject constructor(
    private val context: Context
) {
    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    suspend fun setMessage(message: UIMessage) {
        val messageText = when (message) {
            is UIMessage.SaveError -> context.getString(R.string.saveError)
            is UIMessage.DeleteError -> context.getString(R.string.deleteError)
            is UIMessage.UpdateError -> context.getString(R.string.updateError)
            is UIMessage.LoadError -> context.getString(R.string.loadError)
            is UIMessage.AuthorizationError -> context.getString(R.string.authorizationError)
            is UIMessage.AuthorizationSuccess -> context.getString(R.string.authorizationSuccess)
            is UIMessage.AuthorizationReset -> context.getString(R.string.authorizationReset)
        }

        Log.d("UIErrorStorage", "setError: $messageText")
        _message.update { messageText }
        delay(2000)
        _message.update { null }
    }

    /**
     * Represents the specific UI messages that can be displayed to the user.
     *
     * This sealed class encapsulates various types of UI messages, including errors and success notifications,
     * each with a unique text message. These messages are used throughout the application to communicate
     * different states or outcomes of operations to the user, enhancing the interactive experience.
     */

    sealed class UIMessage {
        data object SaveError : UIMessage()
        data object DeleteError : UIMessage()
        data object UpdateError : UIMessage()
        data object LoadError : UIMessage()
        data object AuthorizationError : UIMessage()
        data object AuthorizationSuccess : UIMessage()
        data object AuthorizationReset : UIMessage()
    }
}
