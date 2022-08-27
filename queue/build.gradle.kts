import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

apply<ShadowPlugin>()

dependencies {
  implementation(project(":common"))
  implementation(project(":minestom-common"))

  implementation(libs.grpc.protobuf)
  implementation(libs.grpc.stub)
  implementation(libs.grpc.netty)
  implementation(libs.guice)
  implementation(libs.agones4j)
  implementation(libs.terminable)
  implementation(libs.redis)
  implementation(libs.caffeine)
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

  build {
    dependsOn("shadowJar")
  }
}
