enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ToDoApp"
include(":app")
include(":core:todo-api")
include(":core:common")
include(":core:database")
include(":core:data")
include(":features:common")
include(":core:ui-kit")
include(":features:todo-details")
include(":features:todo-list")
include(":features:todo-auth")
include(":features:todo-settings")
include(":features:todo-about")
