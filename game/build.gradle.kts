import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("application")
}

@Suppress("UNCHECKED_CAST")
val jvmArgs = rootProject.extra["jvmArgs"] as List<String>
val lwjglNatives = rootProject.extra["lwjglNatives"] as String

dependencies {
    implementation(project(":api"))
}

application {
    mainClass.set("net.skittles.Main")
    applicationDefaultJvmArgs = jvmArgs
}

tasks.jar {
    archiveBaseName.set("skittles-game")

    manifest {
        attributes["Main-Class"] = application.mainClass.get()
        attributes["Class-Path"] = configurations.runtimeClasspath.get()
            .joinToString(" ") { it.name }
    }
}

tasks.register<Copy>("copyDependencies") {
    group = "build"
    description = "Copies all runtime dependencies to game/build/libs"
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("libs"))
}

tasks.register("buildExecutable") {
    group = "build"
    description = "Builds the game jar and copies all dependencies to game/build/libs"
    dependsOn(tasks.jar, "copyDependencies")

    doLast {
        val outDir = layout.buildDirectory.dir("libs").get().asFile
        val jvmArgsString = jvmArgs.joinToString(" ")
        val jarName = "skittles-game-${project.version}.jar"

        file("$outDir/run.bat").writeText(
            "@echo off\r\njava $jvmArgsString -jar $jarName\r\npause\r\n"
        )

        val sh = file("$outDir/run.sh")
        sh.writeText("#!/bin/sh\njava $jvmArgsString -jar $jarName\n")
        sh.setExecutable(true)

        println("Executable built to: $outDir")
    }
}