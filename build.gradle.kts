plugins {
    `java-library`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.3.1"
}

group = "studio.o7"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation("com.gradleup.shadow:shadow-gradle-plugin:8.3.7")
    implementation("net.thebugmc.gradle.sonatype-central-portal-publisher:net.thebugmc.gradle.sonatype-central-portal-publisher.gradle.plugin:1.2.4")
    implementation("org.cthing.build-constants:org.cthing.build-constants.gradle.plugin:2.0.0")

    compileOnly(gradleApi())
    testImplementation(gradleTestKit())

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.13.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.2")

    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.cthing:assertj-gradle:3.0.0")
}

gradlePlugin {
    website = "https://github.com/o7studios/remora"
    vcsUrl = "https://github.com/o7studios/remora"

    plugins.create("remora") {
        id = "studio.o7.remora"
        implementationClass = "studio.o7.remora.RemoraPlugin"
        displayName = "Remora"
        description = "o7studios helper plugin for Gradle projects"
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