plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.theprogmatheus.mc.plugin"
version = "1.0-SNAPSHOT"

val pluginPackage = "${group}.spawnerx"
val pluginMain = "${pluginPackage}.SpawnerX"
val pluginName = project.name
val pluginVersion = project.version
val pluginAuthors = listOf("Sr_Edition", "TheProgMatheus")
val apiVersion = "1.20"
val pluginWebsite = "https://github.com/theprogmatheus/SpawnerX"
val pluginDescription = "A spawner plugin for your paper server"
val pluginDependencies = listOf(
    "javax.inject:javax.inject:1",
    "net.gmcbm.dependencies:acf-paper:0.5.2",
    "com.j256.ormlite:ormlite-jdbc:6.1",
    "com.zaxxer:HikariCP:6.1.0",
    "com.h2database:h2:2.3.232"
)// Repository: https://repo.papermc.io/
val supportsLibraries = true

repositories {
    mavenCentral()
    maven {
        name = "spigot-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.38")
    compileOnly("org.jetbrains:annotations:24.1.0")

    implementation("com.github.theprogmatheus:JGR-UChecker:1.0.0") {
        isTransitive = false;
    }

    annotationProcessor("org.projectlombok:lombok:1.18.38")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.9.0")

    if (!(supportsLibraries))
        pluginDependencies.forEach(this::implementation)
    else
        pluginDependencies.forEach { dependency ->
            compileOnly(dependency)
            testImplementation(dependency)
        }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {

    processResources {
        filesMatching("plugin.yml") {
            expand(
                "main" to pluginMain,
                "name" to pluginName,
                "version" to pluginVersion,
                "apiVersion" to apiVersion,
                "authors" to pluginAuthors.joinToString("\n  - "),
                "website" to pluginWebsite,
                "description" to pluginDescription,
                "libraries" to if ((!supportsLibraries) || pluginDependencies.isEmpty()) "[]" else pluginDependencies.joinToString(
                    separator = "\n - ",
                    prefix = "\n - "
                ),
            )

        }
    }

    shadowJar {
        dependsOn(test)
        mustRunAfter(test)

        relocate("com.github.theprogmatheus.util", "${pluginPackage}.util")
    }

    test {
        dependsOn(processResources)
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
        failFast = true
    }

    build {
        dependsOn(shadowJar)
    }
}