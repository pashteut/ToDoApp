plugins {
    id("android-core-lib-convention")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.pashteut.todoapp.core.ui_kit"
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(libs.material)
    api(libs.androidx.material3)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.ui.tooling)
    api(libs.androidx.lifecycle.runtime.compose)
}