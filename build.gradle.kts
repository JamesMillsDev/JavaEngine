import org.gradle.internal.os.OperatingSystem

plugins {
    id("java")
}

val lwjglVersion = "3.3.4"
val lwjglNatives = when {
    OperatingSystem.current().isWindows -> "natives-windows"
    OperatingSystem.current().isMacOsX  -> "natives-macos"
    else                                -> "natives-linux"
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

// Must be set before subprojects/game build scripts read them
extra["lwjglVersion"] = lwjglVersion
extra["lwjglNatives"]  = lwjglNatives
extra["jvmArgs"]       = jvmArgs

subprojects {
    apply(plugin = "java")

    group = "net.skittles"
    version = "1.0-SNAPSHOT"

    val lwjglVersion = rootProject.extra["lwjglVersion"] as String
    val lwjglNatives = rootProject.extra["lwjglNatives"] as String

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

        "implementation"("org.lwjgl:lwjgl")
        "implementation"("org.lwjgl:lwjgl-glfw")
        "implementation"("org.lwjgl:lwjgl-opengl")
        "implementation"("org.lwjgl:lwjgl-stb")

        "runtimeOnly"("org.lwjgl:lwjgl::$lwjglNatives")
        "runtimeOnly"("org.lwjgl:lwjgl-glfw::$lwjglNatives")
        "runtimeOnly"("org.lwjgl:lwjgl-opengl::$lwjglNatives")
        "runtimeOnly"("org.lwjgl:lwjgl-stb::$lwjglNatives")

        "implementation"("org.yaml:snakeyaml:2.2")

        "testImplementation"(platform("org.junit:junit-bom:5.10.0"))
        "testImplementation"("org.junit.jupiter:junit-jupiter")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.register("generateIdeaRunConfig") {
    group = "ide"
    description = "Generates an IntelliJ IDEA run configuration"

    doLast {
        val runConfigDir = file(".idea/runConfigurations")
        runConfigDir.mkdirs()

        val mainClassName = "net.skittles.Main"
        val jvmArgsString = jvmArgs.joinToString(" ")

        val xml = """
            <component name="ProjectRunConfigurationManager">
              <configuration default="false" name="Run game" type="Application" factoryName="Application">
                <option name="MAIN_CLASS_NAME" value="$mainClassName" />
                <option name="VM_PARAMETERS" value="$jvmArgsString" />
                <option name="WORKING_DIRECTORY" value="${'$'}PROJECT_DIR${'$'}" />
                <module name="game.main" />
                <method v="2">
                  <option name="Make" enabled="true" />
                </method>
              </configuration>
            </component>
        """.trimIndent()

        val configFile = file(".idea/runConfigurations/game.xml")
        configFile.writeText(xml)
        println("Run configuration written to: ${configFile.relativeTo(projectDir)}")
    }
}