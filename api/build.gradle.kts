plugins {
    id("java")
}

// API has no entry point, just produces a library jar
tasks.jar {
    archiveBaseName.set("skittles-api")

    manifest {
        attributes["Implementation-Title"] = "Skittles API"
        attributes["Implementation-Version"] = version
    }
}