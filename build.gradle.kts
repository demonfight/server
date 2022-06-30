import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.spotless.LineEnding

plugins {
  java
  `java-library`
  `maven-publish`
  signing
  id("com.diffplug.spotless") version "6.7.2"
  id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
  id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

allprojects {
  group = "tr.com.infumia"

  extra["qualifiedProjectName"] = if (parent == null) {
    "Server"
  } else {
    val parentName = parent!!.extra["qualifiedProjectName"].toString()
    parentName + name[0].toUpperCase() + name.substring(1)
  }
}

subprojects {
  apply<JavaPlugin>()
  apply<JavaLibraryPlugin>()
  apply<SpotlessPlugin>()

  if (this.name.contains("api")) {
    apply<MavenPublishPlugin>()
    apply<SigningPlugin>()
  }

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
  }

  dependencies {
    compileOnly(libs.lombok)
    compileOnly(libs.annotations)

    annotationProcessor(libs.lombok)
    annotationProcessor(libs.annotations)

    testAnnotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.annotations)
  }

  spotless {
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

nexusPublishing {
  repositories {
    sonatype()
  }
}
