package com.pashteut.todoapp.todo_api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the response structure for a single ToDo item received from the server.
 *
 * This data class encapsulates the response from the server when querying for a single ToDo item. It includes the status of the request,
 * the ToDo item itself encapsulated within an [ElementDTO], and the revision number of the ToDo list.
 *
 * @property status The status of the response, indicating success or failure.
 * @property element The [ElementDTO] object representing the ToDo item.
 * @property revision An integer representing the current revision of the ToDo list.
 */

@Serializable
data class TodoResponseElementDTO(
    @SerialName("status")
    val status: String,
    @SerialName("element")
    val element: ElementDTO,
    @SerialName("revision")
    val revision: Int,
)
