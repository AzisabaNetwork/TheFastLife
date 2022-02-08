plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "net.azisaba"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.javassist:javassist:3.28.0-GA")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<ProcessResources> {
        filteringCharset = "UTF-8"
        from(sourceSets.main.get().resources.srcDirs) {
            include("**")

            val tokenReplacementMap = mapOf("VERSION" to version)

            filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to tokenReplacementMap)
        }

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(projectDir) { include("LICENSE") }
    }

    shadowJar {
        manifest {
            attributes(
                "Premain-Class" to "net.azisaba.theFastLife.TheFastLife",
                "Agent-Class" to "net.azisaba.theFastLife.TheFastLife",
            )
        }

        relocate("javassist", "net.azisaba.paperFix.libs.javassist")
        archiveFileName.set("TheFastLife-${project.version}.jar")
    }
}
