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
            version("gradlePublish", "1.3.0")
            version("lombok", "1.18.36")
            version("shadow", "8.3.5")
            version("mavenCentralPublisher", "1.2.4")

            version("junit", "5.8.1")

            library("lombok", "org.projectlombok", "lombok").versionRef("lombok")
            library("shadow", "com.gradleup.shadow", "shadow-gradle-plugin").versionRef("shadow")
            library("mavenCentralPublisher", "net.thebugmc.gradle.sonatype-central-portal-publisher", "net.thebugmc.gradle.sonatype-central-portal-publisher.gradle.plugin").versionRef("mavenCentralPublisher")

            library("junit-api", "org.junit.jupiter", "junit-jupiter").versionRef("junit")
            library("junit-engine", "org.junit.jupiter", "junit-engine").versionRef("junit")

            plugin("gradlePublish", "com.gradle.plugin-publish").versionRef("gradlePublish")
        }
    }
}