plugins {
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    id("android-app-convention")
    id("tg-plugin")
}

tgReporter{
    token.set(providers.environmentVariable("TG_TOKEN"))
    chatId.set(providers.environmentVariable("TG_CHAT"))
    enableApkCheck.set(true)
    maxApkSize.set(20f)
    enableApkDetails.set(true)
}


android {
    defaultConfig {
        applicationId = "com.pashteut.todoapp"
        version = 1
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {

    implementation(projects.features.todoList)
    implementation(projects.features.todoDetails)
    implementation(projects.features.todoAuth)
    implementation(projects.features.common)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.uiKit)
}