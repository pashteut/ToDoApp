package com.pashteut.todoapp.model.api.dtos

import com.pashteut.todoapp.model.Priority
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a single ToDo item for newtwork requests and responses.
 *
 * This data class encapsulates the properties of a ToDo item, including its unique identifier, text description,
 * priority level, optional deadline, completion status, optional color for UI representation, creation timestamp,
 * last modification timestamp, and the identifier of the last user who updated the item.
 *
 * @property id The unique identifier of the ToDo item.
 * @property text The text description of the ToDo item.
 * @property importance The priority level of the ToDo item, represented by the [Priority] enum.
 * @property deadline The optional deadline timestamp for the ToDo item. Null if no deadline is set.
 * @property done The completion status of the ToDo item. True if completed, false otherwise.
 * @property color The optional color code for UI representation of the ToDo item. Null if no color is set.
 * @property createdAt The creation timestamp of the ToDo item.
 * @property changedAt The last modification timestamp of the ToDo item.
 * @property lastUpdatedBy The identifier of the last user who updated the ToDo item.
 */

@Serializable
data class ElementDTO(
    @SerialName("id")
    val id: String,
    @SerialName("text")
    val text: String,
    @SerialName("importance")
    val importance: Priority,
    @SerialName("deadline")
    val deadline: Long?,
    @SerialName("done")
    val done: Boolean,
    @SerialName("color")
    val color: String?,
    @SerialName("created_at")
    val createdAt: Long,
    @SerialName("changed_at")
    val changedAt: Long,
    @SerialName("last_updated_by")
    val lastUpdatedBy: String,
)