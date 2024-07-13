plugins {
    id("android-core-lib-convention")
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

dependencies {
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    
    api(libs.kotlin.serialization)

    api(libs.ktor.client.core)
    api(libs.ktor.client.android)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.client.logging)
    implementation(libs.slf4j.android)

    implementation(projects.core.common)
}