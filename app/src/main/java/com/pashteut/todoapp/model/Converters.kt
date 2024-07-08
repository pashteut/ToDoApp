package com.pashteut.todoapp.model

import com.pashteut.todoapp.model.api.dtos.ElementDTO

fun ToDoItem.toElementDTO() = ElementDTO(
    id = id,
    text = text,
    importance = priority,
    deadline = deadline,
    done = isDone,
    color = null,
    createdAt = createdDate,
    changedAt = updatedDate ?: createdDate,
    lastUpdatedBy = "phone"
)

fun ElementDTO.toToDoItem() = ToDoItem(
    id = id,
    text = text,
    priority = importance,
    deadline = deadline,
    isDone = done,
    createdDate = createdAt,
    updatedDate = changedAt
)