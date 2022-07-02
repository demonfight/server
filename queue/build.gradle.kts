import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.graalvm.buildtools.gradle.NativeImagePlugin
import org.graalvm.buildtools.gradle.dsl.GraalVMExtension
import org.graalvm.buildtools.gradle.tasks.BuildNativeImageTask

apply<ShadowPlugin>()
apply<NativeImagePlugin>()

dependencies {
  implementation(project(":common"))
  implementation(project(":minestom-common"))

  implementation(libs.terminable)

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
}

tasks {
  jar {
    manifest {
      attributes(
        "Main-Class" to "${project.group}.server.queue.Server",
        "Multi-Release" to true
      )
    }
  }

  withType<ShadowJar> {
    archiveBaseName.set(project.extra["qualifiedProjectName"].toString())
    archiveClassifier.set(null as String?)
  }

  withType<BuildNativeImageTask> {
    classpathJar.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
  }

  build {
    dependsOn("shadowJar")
  }
}

configure<GraalVMExtension> {
  useArgFile.set(false)
  testSupport.set(false)
  binaries {
    named("main") {
      javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.matching("GraalVM Community"))
      })
      imageName.set("server")
      mainClass.set("tr.com.infumia.server.queue.Server")
      useFatJar.set(false)
    }
  }
}
