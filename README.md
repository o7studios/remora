<img width="1024" height="241" alt="remora" src="https://github.com/user-attachments/assets/7a38185d-6ece-44c0-adf2-326a3e2f503b" />

# Remora - o7studios Gradle helper plugin

Remora is a Gradle plugin made for o7studios Gradle projects. It includes
automatic shipping of libraries like *lombok*, publishing to Sonatype repositories
and compiler configurations.

## Configuration

Configure your Gradle module by doing the following:

```kotlin
// build.gradle.kts

plugins {
    id("studio.o7.remora")
}

remora {
    groupId = "studio.o7"
    artifactId = "example-project"
}
```

