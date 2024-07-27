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
    enableApkCheck.set(false)
    maxApkSize.set(20f)
    enableApkDetails.set(false)
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
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.ktor.client.mock)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.mockk.agent)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    androidTestImplementation(libs.mockwebserver)

    implementation(projects.features.todoList)
    implementation(projects.features.todoDetails)
    implementation(projects.features.todoAuth)
    implementation(projects.features.todoSettings)
    implementation(projects.features.todoAbout)
    implementation(projects.features.common)
    implementation(projects.core.data)
    implementation(projects.core.common)
    implementation(projects.core.uiKit)
}