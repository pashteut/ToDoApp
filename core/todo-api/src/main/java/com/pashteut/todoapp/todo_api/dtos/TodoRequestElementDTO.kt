package com.pashteut.todoapp.todo_api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the request structure for sending a single ToDo item to the server.
 *
 * This data class is used to encapsulate a single ToDo item being sent to the server. It is primarily used in network requests
 * where a single item needs to be sent to the server.
 *
 * @property element The [ElementDTO] object representing the ToDo item.
 */

@Serializable
data class TodoRequestElementDTO (
    @SerialName("element")
    val element: ElementDTO,
)
