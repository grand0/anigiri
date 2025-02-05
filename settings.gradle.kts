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
include(":core:nav")

include(":feature:home:api")
include(":feature:home:impl")
include(":feature:release:api")
include(":feature:release:impl")
include(":feature:search:api")
include(":feature:search:impl")
include(":feature:profile:api")
include(":feature:profile:impl")
 
