package com.pashteut.todoapp.data.functions

import com.pashteut.todoapp.data.model.Priority
import com.pashteut.todoapp.data.model.TodoItem
import com.pashteut.todoapp.database.dbos.PriorityDBO
import com.pashteut.todoapp.database.dbos.TodoItemDBO
import com.pashteut.todoapp.todo_api.dtos.ElementDTO
import com.pashteut.todoapp.todo_api.dtos.PriorityDTO

fun TodoItemDBO.toElementDTO() = ElementDTO(
    id = id,
    text = text,
    importance = priority.toPriorityDTO(),
    deadline = deadline,
    done = isDone,
    color = null,
    createdAt = createdDate,
    changedAt = updatedDate ?: createdDate,
    lastUpdatedBy = "phone"
)

fun ElementDTO.toToDoItem() = TodoItemDBO(
    id = id,
    text = text,
    priority = importance.toPriorityDBO(),
    deadline = deadline,
    isDone = done,
    createdDate = createdAt,
    updatedDate = changedAt
)

fun TodoItem.toTodoItemDBO() = TodoItemDBO(
    id = id,
    text = text,
    priority = priority.toPriorityDBO(),
    deadline = deadline,
    isDone = isDone,
    createdDate = createdDate,
    updatedDate = updatedDate
)

fun TodoItemDBO.toTodoItem() = TodoItem(
    id = id,
    text = text,
    priority = priority.toPriority(),
    deadline = deadline,
    isDone = isDone,
    createdDate = createdDate,
    updatedDate = updatedDate
)

fun PriorityDTO.toPriorityDBO() = PriorityDBO.valueOf(name)

fun PriorityDBO.toPriorityDTO() = PriorityDTO.valueOf(name)

fun PriorityDBO.toPriority() = Priority.valueOf(name)

fun Priority.toPriorityDBO() = PriorityDBO.valueOf(name)