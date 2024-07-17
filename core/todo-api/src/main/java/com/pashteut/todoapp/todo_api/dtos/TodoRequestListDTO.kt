package com.pashteut.todoapp.todo_api.dtos

import kotlinx.serialization.Serializable

/**
 * Represents the request structure for patch a list of ToDo items to the server.
 *
 * This data class is used to encapsulate the list of ToDo items being sent to the server for patch items.
 *
 * @property list A list of [ElementDTO] objects representing the ToDo items to be synchronized.
 */

@Serializable
data class TodoRequestListDTO(
    val list: List<ElementDTO>
)
