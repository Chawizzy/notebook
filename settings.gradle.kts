pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // Added after project creation.
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

rootProject.name = "Notebook Android Project"
include(":app")
