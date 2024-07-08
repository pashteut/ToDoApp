package com.pashteut.todoapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enum class representing the priority levels of ToDo items.
 *
 * Defines three levels of priority:
 * - HIGH: Indicates items that are of utmost importance and urgency.
 * - DEFAULT: Represents the standard or normal priority for items.
 * - LOW: Used for items that are of lesser importance and can be attended to later.
 */

@Serializable
enum class Priority {
    @SerialName("important")
    HIGH,

    @SerialName("basic")
    DEFAULT,

    @SerialName("low")
    LOW
}