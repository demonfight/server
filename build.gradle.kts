import com.diffplug.spotless.LineEnding

plugins {
  java
  kotlin("jvm") version "1.7.0"
  id("com.diffplug.spotless") version "6.7.2"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "tr.com.infumia"

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

tasks {
  compileJava {
    options.encoding = Charsets.UTF_8.name()
  }

  processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from(project.the<SourceSetContainer>()["main"].resources.srcDirs) {
      expand("pluginVersion" to project.version)
      include("plugin.yml")
    }
  }

  jar {
    manifest {
      attributes(
        "Main-Class" to "${project.group}.server.Server",
        "Multi-Release" to true
      )
    }
  }

  shadowJar {
    archiveClassifier.set(null as String?)
  }

  build {
    dependsOn(spotlessApply)
    dependsOn(jar)
    dependsOn(shadowJar)
  }
}

repositories {
  mavenCentral()
  maven("https://jitpack.io")
}

dependencies {
  implementation(libs.gson)
  implementation(libs.fastutil)
  implementation(libs.kotlin) {
    exclude("org.jetbrains", "annotations")
  }
  implementation(libs.minestom) {
    exclude("com.google.code.gson", "gson")
    exclude("it.unimi.dsi", "fastutil")
    exclude("org.jetbrains.kotlin", "kotlin-stdlib")
    exclude("org.jetbrains", "annotations")
  }

  compileOnly(libs.lombok)
  compileOnly(libs.annotations)

  annotationProcessor(libs.lombok)
  annotationProcessor(libs.annotations)

  testAnnotationProcessor(libs.lombok)
  testAnnotationProcessor(libs.annotations)
}

spotless {
  lineEndings = LineEnding.UNIX

  java {
    importOrder()
    removeUnusedImports()
    endWithNewline()
    indentWithSpaces(2)
    trimTrailingWhitespace()
    prettier(
      mapOf(
        "prettier" to "2.7.1",
        "prettier-plugin-java" to "1.6.2"
      )
    ).config(
      mapOf(
        "parser" to "java",
        "tabWidth" to 2,
        "useTabs" to false
      )
    )
  }
}
