import org.gradle.api.JavaVersion

object AndroidConst {
    const val NAMESPACE = "com.pashteut.todoapp"
    const val COMPILE_SDK = 34
    const val TARGET_SDK = 34
    const val MIN_SDK = 26
    val COMPILE_JDK_VERSION = JavaVersion.VERSION_1_8
    const val KOTLIN_JVM_TARGET = "1.8"
}