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
    }
}

rootProject.name = "CleanMovieApp"
include(":app")
include(":core-network")
include(":data")
include(":domain")
include(":core-common")
include(":core-database")
include(":core-ui")
