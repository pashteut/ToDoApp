package com.pashteut.todoapp.plugins.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.zip.ZipFile

abstract class ApkDetailsTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:OutputFile
    abstract val apkDetails: RegularFileProperty

    @TaskAction
    fun execute() {
        apkDir.get().asFile.listFiles()
            ?.filter { it.name.endsWith(".apk") }
            ?.forEach { apk ->
                val outputFile = apkDetails.get().asFile
                outputFile.writeText("")
                analysisApk(apk, outputFile)
            }
    }

    private fun analysisApk(apkFile: File, outputFile: File) {
        val zipFile = ZipFile(apkFile)

        zipFile.entries().asSequence().forEach {
            if (!it.isDirectory) {
                val formattedSize = formatFileSize(it.size)
                apkDetails.get().asFile.appendText("${it.name} - $formattedSize\n")
            }
        }
    }

    private fun formatFileSize(sizeInBytes: Long): String {
        val sizeInKB = sizeInBytes / 1024.0
        val sizeInMB = sizeInKB / 1024.0

        return when {
            sizeInMB >= 1 -> String.format("%.2f MB", sizeInMB)
            sizeInKB >= 1 -> String.format("%.2f KB", sizeInKB)
            else -> "$sizeInBytes bytes"
        }
    }
}