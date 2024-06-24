package com.pashteut.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_do_items")
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String,
    val priority: Priority,
    val deadline: Long? = null,
    val isDone: Boolean,
    val createdDate: Long,
    val updatedDate: Long? = null
)