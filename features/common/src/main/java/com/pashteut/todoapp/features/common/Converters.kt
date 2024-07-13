package com.pashteut.todoapp.features.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.convertDateToString(): String {
    val selectedDate = LocalDate.ofEpochDay(this / (24 * 60 * 60 * 1000))
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    return selectedDate.format(formatter)
}