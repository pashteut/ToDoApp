package com.pashteut.todoapp.plugins

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import com.pashteut.todoapp.plugins.tasks.ApkDetailsTask
import com.pashteut.todoapp.plugins.tasks.TgTask
import com.pashteut.todoapp.plugins.tasks.ValidateApkSizeTask
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

class TgPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw GradleException("Android not found")

        val extension = project.extensions.create("tgReporter", TelegramExtension::class)

        val api = TgApi(HttpClient(OkHttp))
        androidComponents.onVariants { variant ->
            val artifacts = variant.artifacts.get(SingleArtifact.APK)

            if (extension.enableApkCheck.get())
                project.tasks.register(
                    "validateApkSizeFor${variant.name.replaceFirstChar { it.uppercase() }}",
                    ValidateApkSizeTask::class,
                    api
                ).configure {
                    apkDir.set(artifacts)
                    currentSizeFile.set(project.layout.buildDirectory.file("outputs/apkSize/${variant.name}.txt"))
                    maxSizeMb.set(extension.maxApkSize)
                    token.set(extension.token)
                    chatId.set(extension.chatId)
                }
            if (extension.enableApkDetails.get())
                project.tasks.register(
                    "tgApkDetailsFor${variant.name.replaceFirstChar { it.uppercase() }}",
                    ApkDetailsTask::class,
                ) {
                    apkDir.set(artifacts)
                    apkDetails.set(project.layout.buildDirectory.file("outputs/apkDetails/${variant.name}.txt"))
                }


            project.tasks.register(
                "tgReportFor${variant.name.replaceFirstChar { it.uppercase() }}",
                TgTask::class,
                api
            ).configure {
                apkDir.set(artifacts)
                appVariant.set(variant.name)
                token.set(extension.token)
                chatId.set(extension.chatId)
                if (extension.enableApkCheck.get()) {
                    currentApkSizeFile.set(project.layout.buildDirectory.file("outputs/apkSize/${variant.name}.txt"))
                    dependsOn(
                        "validateApkSizeFor${variant.name.replaceFirstChar { it.uppercase() }}"
                    )
                }
                if (extension.enableApkDetails.get()) {
                    apkDetailsFile.set(project.layout.buildDirectory.file("outputs/apkDetails/${variant.name}.txt"))
                    dependsOn(
                        "tgApkDetailsFor${variant.name.replaceFirstChar { it.uppercase() }}"
                    )
                }
            }

        }
    }
}
