plugins {
    id("android-core-lib-convention")
    alias(libs.plugins.kapt)
    alias(libs.plugins.dagger.hilt.android)
}

dependencies {
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}