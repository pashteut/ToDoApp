package com.pashteut.todoapp.todo_api.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PriorityDTO {
    @SerialName("important")
    HIGH,

    @SerialName("basic")
    DEFAULT,

    @SerialName("low")
    LOW
}