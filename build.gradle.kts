plugins {
    `java-library`
    `maven-publish`
    id("com.gradle.plugin-publish") version "2.1.1"
}

group = "studio.o7"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")

    implementation("org.yaml:snakeyaml:2.5")

    implementation("com.gradleup.shadow:shadow-gradle-plugin:9.2.2")
    implementation("net.thebugmc.gradle.sonatype-central-portal-publisher:net.thebugmc.gradle.sonatype-central-portal-publisher.gradle.plugin:1.2.4")
    implementation("org.cthing.build-constants:org.cthing.build-constants.gradle.plugin:2.1.0")

    compileOnly(gradleApi())
    testImplementation(gradleTestKit())

    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.3")
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
    testImplementation("org.junit.jupiter:junit-jupiter-params:6.0.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.3")

    testImplementation("org.assertj:assertj-core:3.27.7")
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
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}