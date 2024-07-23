plugins {
    id("android-features-lib-convention")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.pashteut.todoapp.features.todo_about"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.core.uiKit)
    implementation(libs.div)
    implementation(libs.div.core)
    implementation(libs.div.json)
    implementation(libs.div.picasso)
    implementation(libs.div.utils)
}