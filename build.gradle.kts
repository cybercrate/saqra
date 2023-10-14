plugins {
    id("java")

    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

group = "com.wingmann"
version = "1.0-SNAPSHOT"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:32.1.1-jre")

    implementation("com.google.zxing:core:3.5.2")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("com.wingmann.saqra.Main")
}

tasks.jar {
    manifest.attributes["Main-Class"] = application.mainClass
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map { zipTree(it) }

    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
