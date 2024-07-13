plugins {
    `kotlin-dsl`
}

gradlePlugin{
    plugins.register("tg-plugin"){
        id = "tg-plugin"
        implementationClass = "com.pashteut.todoapp.plugins.TgPlugin"
    }
}

dependencies{
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    api(libs.ktor.client.core)
    api(libs.ktor.client.okhttp)
}

