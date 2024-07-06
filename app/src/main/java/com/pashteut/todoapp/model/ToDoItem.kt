package com.pashteut.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single ToDo item in the application.
 *
 * @property id Unique identifier for the ToDo item.
 * @property text The text description of the ToDo item.
 * @property priority The priority level of the ToDo item, affecting its ordering and visibility.
 * @property deadline Optional deadline timestamp for the ToDo item. Null if no deadline is set.
 * @property isDone Boolean flag indicating whether the ToDo item has been completed.
 * @property createdDate Timestamp indicating when the ToDo item was created.
 * @property updatedDate Optional timestamp indicating the last update time of the ToDo item. Null if never updated.
 */

@Entity(tableName = "to_do_items")
data class ToDoItem(
    @PrimaryKey val id: String = "",
    val text: String,
    val priority: Priority,
    val deadline: Long? = null,
    val isDone: Boolean,
    val createdDate: Long,
    val updatedDate: Long? = null
)