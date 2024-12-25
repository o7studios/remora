rootProject.name = "remora"

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("gradlePublish", "1.2.1")
            version("lombok", "1.18.36")
            version("shadow", "8.3.5")

            library("lombok", "org.projectlombok", "lombok").versionRef("lombok")
            library("shadow", "com.gradleup.shadow", "shadow-gradle-plugin").versionRef("shadow")

            plugin("gradlePublish", "com.gradle.plugin-publish").versionRef("gradlePublish")
        }
    }
}