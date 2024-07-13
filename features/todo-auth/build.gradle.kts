plugins {
    id("android-features-lib-convention")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.pashteut.todoapp.features.todo_auth"
    defaultConfig {
        buildConfigField ("String", "REQUEST_TOKEN_URL", "\"https://oauth.yandex.ru/authorize?response_type=token&client_id=0d0970774e284fa8ba9ff70b6b06479a\"")
    }

    buildFeatures{
        buildConfig = true
        compose = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.features.common)
    implementation(projects.core.uiKit)
}