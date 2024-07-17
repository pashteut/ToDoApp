package com.pashteut.todoapp.plugins.tasks

import com.pashteut.todoapp.plugins.TgApi
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class ValidateApkSizeTask @Inject constructor(
    private val tgApi: TgApi,
) : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val maxSizeMb: Property<Float>

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @get:OutputFile
    abstract val currentSizeFile: RegularFileProperty

    @TaskAction
    fun validate() {
        val token = token.get()
        val chatId = chatId.get()
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                val sizeMb = it.length().toFloat() / (1024 * 1024).toFloat()
                val size = String.format("%.2f", sizeMb)
                val maxSize = String.format("%.2f", maxSizeMb.get())
                if (sizeMb > maxSizeMb.get()) {
                    runBlocking {
                        tgApi.sendMessage(
                            "App is more than $maxSize Mb, current size is $size Mb",
                            token,
                            chatId
                        )
                    }
                    throw GradleException("APK size exceeds limit: ${size}MB (limit: ${maxSizeMb.get()}MB)")
                } else
                    currentSizeFile.get().asFile.writeText(size)
            }
    }
}