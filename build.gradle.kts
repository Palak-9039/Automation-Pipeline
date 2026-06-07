plugins {
    kotlin("jvm") version "2.2.20"
    // Adding the serialization plugin matching your version
    kotlin("plugin.serialization") version "2.2.20"
    // Adding the Shadow Jar plugin for packaging
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Ktor Core & CIO engine
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")

    // Ktor Content Negotiation and JSON serializer
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")

    // Kotlin Coroutines for running async requests
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.getByName<Jar>("shadowJar") {
    manifest {
        attributes["Main-Class"] = "com.automation.MainKt"
    }
}
