package com.pashteut.todoapp.features.todo_list.ui_logic

import com.pashteut.todoapp.data.model.Priority

data class TodoItemUI (
    val id: String,
    val text: String,
    val priority: Priority,
    val deadline: Long? = null,
    val isDone: Boolean,
)

