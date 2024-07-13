package com.pashteut.todoapp.plugins.tasks

import com.pashteut.todoapp.plugins.TgApi
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class TgTask @Inject constructor(
    private val tgApi: TgApi,
) : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:InputFile
    @get:Optional
    abstract val currentApkSizeFile: RegularFileProperty

    @get:InputFile
    @get:Optional
    abstract val apkDetailsFile: RegularFileProperty

    @get:Input
    abstract val token: Property<String>

    @get:Input
    abstract val chatId: Property<String>

    @get:Input
    abstract val appVariant: Property<String>

    @TaskAction
    fun execute() {
        val token = token.get()
        val chatId = chatId.get()
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach {
                runBlocking {
                    if(currentApkSizeFile.isPresent){
                        val currentSize = currentApkSizeFile.get().asFile.readText()
                        tgApi.sendMessage("Build finished\nApk size is $currentSize Mb", token, chatId)
                    }
                    else
                        tgApi.sendMessage("Build finished", token, chatId)

                    tgApi.sendFileWithNewName(
                        file = it,
                        newFileName = "todolist-${appVariant.get()}-${AndroidConst.VERSION_CODE}.apk",
                        token = token,
                        chatId = chatId
                    )

                    if (apkDetailsFile.isPresent)
                        apkDetailsFile.get().asFile.let {
                            tgApi.sendFileWithNewName(it, "${appVariant.get()}-apk-details.txt", token, chatId)
                        }
                }
            }
    }
}