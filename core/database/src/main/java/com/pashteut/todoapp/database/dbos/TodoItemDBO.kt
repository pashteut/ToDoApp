package com.pashteut.todoapp.database.dbos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_do_items")
data class TodoItemDBO(
    @PrimaryKey val id: String = "",
    val text: String,
    val priority: PriorityDBO,
    val deadline: Long? = null,
    val isDone: Boolean,
    val createdDate: Long,
    val updatedDate: Long? = null
)