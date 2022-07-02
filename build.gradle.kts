import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.spotless.LineEnding

plugins {
  java
  id("com.diffplug.spotless") version "6.8.0" apply false
  id("com.github.johnrengelman.shadow") version "7.1.2" apply false
  id("org.graalvm.buildtools.native") version "0.9.12" apply false
}

allprojects {
  group = "tr.com.infumia"

  extra["qualifiedProjectName"] = if (parent == null) {
    "Server"
  } else {
    val parentName = parent!!.extra["qualifiedProjectName"].toString()
    var current = name[0].toUpperCase() + name.substring(1)
    var index: Int? = 0
    while (index != null) {
      index = current.indexOf('-')
      if (index == -1) {
        break
      }
      current = current.substring(0, index) + current[index + 1].toUpperCase() + current.substring(index + 2)
    }
    parentName + current
  }
}

subprojects {
  apply<JavaPlugin>()
  apply<SpotlessPlugin>()

  java {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
    }
  }

  tasks {
    compileJava {
      options.encoding = Charsets.UTF_8.name()
    }

    jar {
      archiveBaseName.set(project.extra["qualifiedProjectName"].toString())
    }

    build {
      dependsOn(jar)
    }
  }

  repositories {
    mavenCentral()
    maven("https://jitpack.io")
  }

  dependencies {
    compileOnly(rootProject.libs.lombok)
    compileOnly(rootProject.libs.annotations)

    annotationProcessor(rootProject.libs.lombok)
    annotationProcessor(rootProject.libs.annotations)

    testAnnotationProcessor(rootProject.libs.lombok)
    testAnnotationProcessor(rootProject.libs.annotations)
  }

  configure<SpotlessExtension> {
    lineEndings = LineEnding.UNIX
    isEnforceCheck = false

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
}
