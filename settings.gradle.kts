pluginManagement {
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

rootProject.name = "Anigiri"

include(":app")

include(":core:designsystem")
include(":core:db")
include(":core:network")

include(":feature:home:api")
include(":feature:home:impl")
include(":feature:release:api")

include(":feature:release:impl")

include(":core:nav")

include(":feature:search:api")
 
