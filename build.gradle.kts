plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(gradleApi())
    testImplementation(gradleTestKit())
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))