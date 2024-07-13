package com.pashteut.todoapp.plugins

import org.gradle.api.provider.Property

interface TelegramExtension {
    val token: Property<String>
    val chatId: Property<String>
    val enableApkCheck: Property<Boolean>
    val maxApkSize: Property<Float>
    val enableApkDetails: Property<Boolean>
}