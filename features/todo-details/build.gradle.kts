plugins {
    id("android-features-lib-convention")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.pashteut.todoapp.features.todo_details"
    buildFeatures{
        compose = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.features.common)
    implementation(projects.core.uiKit)
}