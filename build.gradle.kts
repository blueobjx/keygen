plugins {
    id("java-library")
}

group = "biznuvo"
version = "1.0"

repositories {
    mavenCentral()
}

//dependencies {
//    testImplementation(platform("org.junit:junit-bom:5.10.0"))
//    testImplementation("org.junit.jupiter:junit-jupiter")
//}
//
//tasks.test {
//    useJUnitPlatform()
//}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to "KeyGen"
            )
        )
    }
}

val createExecScriptTask = tasks.register("createExecScript") {
    dependsOn("jar")

    doLast {
        val execShell = layout.buildDirectory.file("libs/${rootProject.name}").get().asFile
        execShell.writeText("#!/bin/sh\n\nexec java -jar \"\$0\" \"\$@\"\n\n")
        execShell.appendBytes(tasks.jar.get().outputs.files.singleFile.readBytes())
        execShell.setExecutable(true)
    }
}

tasks.register<Tar>("dist") {
    dependsOn(createExecScriptTask)

    compression = Compression.GZIP

    archiveBaseName.set(rootProject.name)
    archiveVersion.set(version.toString())
    archiveExtension.set("tar.gz")

    from(projectDir) {
        include("README.md")
    }

    from(layout.buildDirectory.dir("libs")) {
        include("**/${rootProject.name}")

        rename { "keygen" }

        filePermissions { unix("rwxr-xr-x") }
    }

    into("${rootProject.name}-${version}")
}