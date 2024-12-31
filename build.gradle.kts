plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.gradlePublish)
}

group = "studio.o7"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    implementation(libs.shadow)
    implementation(libs.mavenCentralPublisher)

    compileOnly(gradleApi())
    testImplementation(gradleTestKit())

    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
}

gradlePlugin {
    website = "https://github.com/o7studios/remora"
    vcsUrl = "https://github.com/o7studios/remora"

    plugins.create("remora") {
        id = "studio.o7.remora"
        implementationClass = "studio.o7.remora.RemoraPlugin"
        displayName = "Remora"
        description = "o7studios helper plugin for Gradle projects."
        tags = listOf("maven", "o7studios", "maven-publish", "sonatype", "lombok", "fastutils")
    }
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        mavenLocal()
    }
    publications {
        create<MavenPublication>("pluginMaven")
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}