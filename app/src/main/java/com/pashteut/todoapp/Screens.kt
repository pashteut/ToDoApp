package com.pashteut.todoapp

import kotlinx.serialization.Serializable

@Serializable
object ScreenMain

@Serializable
data class ScreenDetail(val id: String? = null)

@Serializable
object ScreenAuth

@Serializable
object ScreenSettings

@Serializable
object ScreenAbout