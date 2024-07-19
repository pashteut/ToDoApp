plugins {
    id("android-features-lib-convention")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.pashteut.todoapp.features.todo_settings"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.features.common)
    implementation(projects.core.uiKit)
    implementation(projects.core.common)
    implementation(projects.core.data)
}