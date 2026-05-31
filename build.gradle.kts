import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
    id("application")
}

group = "net.skittles"
version = "1.0-SNAPSHOT"

// Detect the current platform for LWJGL natives
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
    // LWJGL BOM — pins all module versions
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    // Core modules — add/remove as needed
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-glfw")       // windowing & input
    implementation("org.lwjgl", "lwjgl-opengl")     // OpenGL
    implementation("org.lwjgl", "lwjgl-stb")        // image loading, fonts, etc.

    // Natives for the current platform
    runtimeOnly("org.lwjgl", "lwjgl",        classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw",   classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb",    classifier = lwjglNatives)

    // Tests
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    // Change this to your actual main class
    mainClass.set("net.skittles.Main")

    // Required on macOS: GLFW must run on the main thread
    if (OperatingSystem.current().isMacOsX) {
        applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
    }
}

tasks.test {
    useJUnitPlatform()
}

// Generate IntelliJ run configuration
tasks.register("generateIdeaRunConfig") {
    group = "ide"
    description = "Generates an IntelliJ IDEA run configuration"

    doLast {
        val runConfigDir = file(".idea/runConfigurations")
        runConfigDir.mkdirs()

        val mainClassName = application.mainClass.get()
        val isMac = OperatingSystem.current().isMacOsX
        val jvmArgs = if (isMac) "-XstartOnFirstThread" else ""

        val configName = "Run ${project.name}"
        val xml = """
            <component name="ProjectRunConfigurationManager">
              <configuration default="false" name="$configName" type="Application" factoryName="Application">
                <option name="MAIN_CLASS_NAME" value="$mainClassName" />
                <option name="VM_PARAMETERS" value="$jvmArgs" />
                <option name="WORKING_DIRECTORY" value="${'$'}PROJECT_DIR${'$'}" />
                <module name="${project.name}.main" />
                <method v="2">
                  <option name="Make" enabled="true" />
                </method>
              </configuration>
            </component>
        """.trimIndent()

        val configFile = file("$runConfigDir/${project.name}.xml")
        configFile.writeText(xml)
        println("Run configuration written to: ${configFile.relativeTo(projectDir)}")
    }
}