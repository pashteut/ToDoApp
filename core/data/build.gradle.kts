plugins {
    id("android-core-lib-convention")
    alias(libs.plugins.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

android {
    defaultConfig {
        buildConfigField ("String", "DEFAULT_APITOKEN", "\"Bearer Thalion\"")
    }

    buildFeatures{
        buildConfig = true
    }
}

dependencies {
    implementation(libs.datastore.preferences)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    api(libs.work.runtime.ktx)
    implementation(projects.core.todoApi)
    implementation(projects.core.database)
    implementation(projects.core.common)
}