package com.pashteut.todoapp.features.todo_list.ui_logic

import com.pashteut.todoapp.data.model.TodoItem

fun TodoItem.toTodoItemUI() =
    TodoItemUI(
        id = id,
        text = text,
        priority = priority,
        deadline = deadline,
        isDone = isDone
    )

