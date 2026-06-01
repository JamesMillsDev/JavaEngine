import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("application")
}

group = "net.skittles"
version = "1.0-SNAPSHOT"

val lwjglVersion = "3.3.4"
val lwjglNatives = when {
    OperatingSystem.current().isWindows -> "natives-windows"
    OperatingSystem.current().isMacOsX  -> "natives-macos"
    else                                -> "natives-linux"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-stb")

    runtimeOnly("org.lwjgl", "lwjgl",        classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw",   classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb",    classifier = lwjglNatives)

    implementation("org.yaml:snakeyaml:2.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val jvmArgs = buildList {
    add("--enable-native-access=ALL-UNNAMED")
    add("--add-opens=java.base/java.lang=ALL-UNNAMED")
    add("--add-opens=java.base/java.nio=ALL-UNNAMED")
    add("--add-opens=java.base/sun.nio.ch=ALL-UNNAMED")

    if (OperatingSystem.current().isMacOsX)
    {
        add("-XstartOnFirstThread")
    }
}

application {
    mainClass.set("net.skittles.Main")
    applicationDefaultJvmArgs = jvmArgs
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("generateIdeaRunConfig") {
    group = "ide"
    description = "Generates an IntelliJ IDEA run configuration"

    doLast {
        val runConfigDir = file(".idea/runConfigurations")
        runConfigDir.mkdirs()

        val mainClassName = application.mainClass.get()
        val jvmArgsString = jvmArgs.joinToString(" ")

        val xml = """
            <component name="ProjectRunConfigurationManager">
              <configuration default="false" name="Run ${project.name}" type="Application" factoryName="Application">
                <option name="MAIN_CLASS_NAME" value="$mainClassName" />
                <option name="VM_PARAMETERS" value="$jvmArgsString" />
                <option name="WORKING_DIRECTORY" value="${'$'}PROJECT_DIR${'$'}" />
                <module name="${project.name}.main" />
                <method v="2">
                  <option name="Make" enabled="true" />
                </method>
              </configuration>
            </component>
        """.trimIndent()

        val configFile = file(".idea/runConfigurations/${project.name}.xml")
        configFile.writeText(xml)
        println("Run configuration written to: ${configFile.relativeTo(projectDir)}")
    }
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
        attributes["Class-Path"] = configurations.runtimeClasspath.get()
            .joinToString(" ") { it.name }
    }
}

tasks.register<Copy>("copyDependencies") {
    group = "build"
    description = "Copies all runtime dependencies to build/libs"
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("libs"))
}

tasks.register("buildExecutable") {
    group = "build"
    description = "Builds the jar and copies all dependencies to build/libs"
    dependsOn(tasks.jar, "copyDependencies")

    doLast {
        println("Executable built to: ${layout.buildDirectory.dir("libs").get()}")
    }
}