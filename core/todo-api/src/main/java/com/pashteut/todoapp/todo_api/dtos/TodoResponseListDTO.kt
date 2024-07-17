package com.pashteut.todoapp.todo_api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the response structure for a list of ToDo items received from the server.
 *
 * @property status The status of the response, indicating success or failure.
 * @property list A list of [ElementDTO] objects representing the ToDo items.
 * @property revision An integer representing the current revision of the ToDo list.
 */

@Serializable
data class TodoResponseListDTO(
    @SerialName("status")
    val status: String,
    @SerialName("list")
    val list: List<ElementDTO>,
    @SerialName("revision")
    val revision: Int,
)
