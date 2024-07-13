plugins {
    id("android-core-lib-convention")
    alias(libs.plugins.ksp)
}

dependencies {
    api(libs.room.runtime)
    api(libs.room.ktx)
    ksp(libs.room.compiler)
}