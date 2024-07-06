package com.pashteut.todoapp.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

/**
 * Provides a centralized set of coroutine dispatchers for use throughout the application.
 *
 * This class encapsulates the various coroutine dispatchers used for executing coroutines in different threads.
 * It allows for a more structured and consistent approach to coroutine execution by providing predefined dispatchers
 * for default, IO, main, and unconfined contexts.
 *
 * @property default The dispatcher for use with computational or CPU-intensive operations.
 * @property io The dispatcher for use with IO-intensive operations, such as file reading and writing, networking, etc.
 * @property main The main thread dispatcher, primarily used for interacting with the UI and performing quick operations.
 * @property unconfined A dispatcher that is not confined to any specific thread. It executes the coroutine in the current thread until the first suspension, after which it resumes in the suspending thread.
 */

class AppDispatchers(
    val default: CoroutineDispatcher = Dispatchers.Default,
    val io: CoroutineDispatcher = Dispatchers.IO,
    val main: MainCoroutineDispatcher = Dispatchers.Main,
    val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
)