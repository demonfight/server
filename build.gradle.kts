import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.spotless.LineEnding
import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.graalvm.buildtools.gradle.NativeImagePlugin
import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import org.graalvm.buildtools.gradle.tasks.BuildNativeImageTask

plugins {
  java
  kotlin("jvm") version "1.7.0"
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
    parentName + name[0].toUpperCase() + name.substring(1)
  }

  println(extra["qualifiedProjectName"])
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

    build {
      dependsOn("spotlessApply")
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
