plugins {
    id("android-features-lib-convention")
    alias(libs.plugins.dagger.hilt.android)
}
android{
    namespace = "com.pashteut.todoapp.features.common"
}
dependencies {
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}