package com.pashteut.todoapp.data.model

data class TodoItem (
    val id: String = "",
    val text: String,
    val priority: Priority,
    val deadline: Long? = null,
    val isDone: Boolean,
    val createdDate: Long,
    val updatedDate: Long? = null
)